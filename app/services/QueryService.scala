package services

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import javax.inject.Inject
import models._
import org.joda.time.DateTime
import sangria.schema._
import services.impl.NBPCurrencyService

import scala.concurrent.Future

class QueryService @Inject() (
  val currencyRateRegressionService: CurrencyRateRegressionService,
  val nBPCurrencyService: NBPCurrencyService
) {}

object QueryService {

  import Actors._

  val QueryType = ObjectType("Query", fields[QueryService, Unit](
    Field("currencies", ListType(Currency.CurrencyType),
      description = Some("Returns list of all currencies"),
      resolve = _.ctx.nBPCurrencyService.fetchCurrencyList().runWith(Sink.seq).map(_.toList)
    ),

    Field("exchanges", ListType(LatestExchangeRates.LatestExchangeRatesType),
      description = Some("Returns latest exchange rates"),
      resolve = _.ctx.nBPCurrencyService.fetchLatestExchangeRates(None).runWith(Sink.seq).map(_.toList)
    ),

    Field("currencyFromPeriod", CurrencyPeriodData.CurrencyPeriodDataType,
      description = Some("Returns currency given currency rates from period"),
      arguments = List(Argument("code", StringType), Argument("from", DateConverter.DateTimeType), Argument("to", DateConverter.DateTimeType)),
      resolve = e => {

        val currency: Currency = Currency("", e.arg("code"))
        val from: DateTime = e.arg("from")
        val to: DateTime = e.arg("to")

        // Not the best but for some reason scala cant figure out the correct type
        val x: Future[CurrencyPeriodData] = e.ctx.nBPCurrencyService.fetchCurrencyDataFromPeriod(
          CurrencyPeriod(currency, from, to)
        ).runWith(Sink.head)

        x
      }),

    Field("currencyFromPeriodTrend", CurrencyPeriodData.CurrencyPeriodDataType,
      description = Some("Returns calculated trend (linear regresion) for currency exchange rate from period"),
      arguments = List(Argument("code", StringType), Argument("from", DateConverter.DateTimeType), Argument("to", DateConverter.DateTimeType)),
      resolve = e => {
        val currency: Currency = Currency("", e.arg("code"))
        val from: DateTime = e.arg("from")
        val to: DateTime = e.arg("to")
        val x: Future[CurrencyPeriodData] = e.ctx.nBPCurrencyService.fetchCurrencyDataFromPeriod(
          CurrencyPeriod(currency, from, to)
        ).map(e.ctx.currencyRateRegressionService.calculateRegression)
          .runWith(Sink.head)
        x
      }
    )
  ))

  val schema = Schema(QueryType)
}
