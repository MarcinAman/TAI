package services.impl

import java.text.SimpleDateFormat

import akka.NotUsed
import akka.stream.scaladsl.Source
import javax.inject.Inject
import models._
import org.joda.time.Period
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import services.CurrencyService

class NBPCurrencyService @Inject() (ws: WSClient) extends CurrencyService {
  override def fetchLatestExchangeRates(table: Option[CurrencyTable]): Source[LatestExchangeRates, NotUsed] =
    table.fold(latestCurrencyRateFromTable(TableA).merge(latestCurrencyRateFromTable(TableB)))(tb => latestCurrencyRateFromTable(tb))

  override def fetchCurrencyList(): Source[Currency, NotUsed] =
    fetchLatestExchangeRates()
      .flatMapConcat(e => Source.fromIterator(() => e.rates.iterator))
      .map(e => e.currency)

  override def fetchCurrencyDataFromPeriod(period: CurrencyPeriod): Source[CurrencyPeriodData, NotUsed] = {
    Source.fromIterator(() => splitPeriodByMaxTime(period).iterator).flatMapConcat(fetchCurrencyByPeriod)
  }

  private def fetchCurrencyByPeriod(period: CurrencyPeriod): Source[CurrencyPeriodData, NotUsed] = {
    val rqs: WSRequest = request(periodExchangeURL(period))

    singleGetRequest(rqs).map(_.json).map(parseCurrencyPeriodData)
  }

  private def latestCurrencyRateFromTable(table: CurrencyTable): Source[LatestExchangeRates, NotUsed] = {
    val rqs: WSRequest = request(exchangesURL(table))

    singleGetRequest(rqs).map(_.json).map(parseResponseToLatestExchangeRates)
  }

  private def parseResponseToLatestExchangeRates(value: JsValue): LatestExchangeRates = {
    val latestExchange = value.validate[Seq[LatestExchangeDTO]] match {
      case s : JsSuccess[Seq[LatestExchangeDTO]] => s.get.head
      case e : JsError => throw new RuntimeException(s"Failed to validate JSON: ${e.errors}")
    }

    val latestRates = latestExchange.rates.map(e => {
      val currency = Currency(e.currency, e.code)
      ExchangeRate(currency, e.mid)
    })

    LatestExchangeRates(latestExchange.no, latestExchange.effectiveDate, latestRates)
  }

  private def parseCurrencyPeriodData(value: JsValue): CurrencyPeriodData = ???

  private def exchangesURL(table: CurrencyTable): String = s"http://api.nbp.pl/api/exchangerates/tables/${table.code}?format=json"

  private def periodExchangeURL(currencyPeriod: CurrencyPeriod): String = {
    val formatter = new SimpleDateFormat("yyyy-MM-dd")

    s"http://api.nbp.pl/api/exchangerates/rates/a/${currencyPeriod.currency.code}/${formatter.format(currencyPeriod.from)}/${formatter.format(currencyPeriod.to)}/?format=json"
  }

  // TODO dear god this is shit
  private def splitPeriodByMaxTime(currencyPeriod: CurrencyPeriod, maxPeriodInDays: Int = 93): Seq[CurrencyPeriod] = {
    val periodInDays = new Period(currencyPeriod.from, currencyPeriod.to).getDays

    val x = (periodInDays.floatValue() / periodInDays.floatValue()).ceil.toInt

    var periods = List.empty[CurrencyPeriod]
    var begining = currencyPeriod.from
    for (_ <- 0 until x){
      val beg = begining
      val end = beg.plusDays(maxPeriodInDays)

      if(end.isAfter(currencyPeriod.to)){
        periods = CurrencyPeriod(currencyPeriod.currency, beg, currencyPeriod.to) :: periods
        begining = currencyPeriod.to
      } else {
        periods = CurrencyPeriod(currencyPeriod.currency, beg, end) :: periods
        begining = end
      }
    }

    periods
  }

  private def request(url: String): WSRequest = ws.url(url)
    .addHttpHeaders("Accept" -> "application/json")

  private def singleGetRequest(request: WSRequest): Source[WSResponse, NotUsed] =
    Source.fromFuture{
      request.get()
    }
}
