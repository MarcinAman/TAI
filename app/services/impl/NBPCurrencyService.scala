package services.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import javax.inject.Inject
import models._
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

  override def fetchCurrencyDataFromPeriod(period: CurrencyPeriod): Source[CurrencyPeriodData, NotUsed] = ???

  private def latestCurrencyRateFromTable(table: CurrencyTable): Source[LatestExchangeRates, NotUsed] = {
    val request: WSRequest = ws.url(exchangesURL(table))
      .addHttpHeaders("Accept" -> "application/json")

    singleGetRequest(request).map {
      response => response.json
    }.map(parseResponseToLatestExchangeRates)
  }

  def parseResponseToLatestExchangeRates(value: JsValue): LatestExchangeRates = {
    val latestExchange = value.validate[LatestExchangeDTO] match {
      case s : JsSuccess[LatestExchangeDTO] => s.get
      case e : JsError => throw new RuntimeException(s"Failed to validate JSON: ${e.toString}")
    }

    val latestRates = latestExchange.rates.map(e => {
      val currency = Currency(e.currency, e.code)
      ExchangeRate(currency, e.mid)
    })

    LatestExchangeRates(latestExchange.no, latestExchange.effectiveDate, latestRates)
  }

  private def exchangesURL(table: CurrencyTable): String = s"http://api.nbp.pl/api/exchangerates/tables/${table.code}?format=json"

  private def singleGetRequest(request: WSRequest): Source[WSResponse, NotUsed] =
    Source.fromFuture{
      request.get()
    }
}
