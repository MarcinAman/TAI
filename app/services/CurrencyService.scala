package services

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import models._
import org.joda.time.DateTime
import sangria.schema._

import scala.concurrent.Future

trait CurrencyService {
  def fetchLatestExchangeRates(table: Option[CurrencyTable] = None): Source[LatestExchangeRates, NotUsed]

  def fetchCurrencyList(): Source[Currency, NotUsed]

  def fetchCurrencyDataFromPeriod(period: CurrencyPeriod): Source[CurrencyPeriodData, NotUsed]
}

object CurrencyService {

  import Actors._

  val QueryType = ObjectType("Query", fields[CurrencyService, Unit](
    Field("currencies", ListType(Currency.CurrencyType),
      description = Some("Returns list of all currencies"),
      resolve = _.ctx.fetchCurrencyList().runWith(Sink.seq).map(_.toList)
    ),

    Field("exchanges", ListType(LatestExchangeRates.LatestExchangeRatesType),
      description = Some("Returns latest exchange rates"),
      resolve = _.ctx.fetchLatestExchangeRates().runWith(Sink.seq).map(_.toList)
    ),

    Field("currencyFromPeriod", CurrencyPeriodData.CurrencyPeriodDataType,
      description = Some("Returns currency given currency rates from period"),
      arguments = List(Argument("code", StringType), Argument("from", DateConverter.DateTimeType), Argument("to", DateConverter.DateTimeType)),
      resolve = e => {

        val currency: Currency = Currency("", e.arg("code"))
        val from: DateTime = e.arg("from")
        val to: DateTime = e.arg("to")

        // Not the best but for some reason scala cant figure out the correct type
        val x: Future[CurrencyPeriodData] = e.ctx.fetchCurrencyDataFromPeriod(CurrencyPeriod(currency, from, to)).runWith(Sink.head)

        x
      })
  ))

  val schema = Schema(QueryType)
}
