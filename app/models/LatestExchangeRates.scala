package models

import org.joda.time.DateTime
import sangria.macros.derive._
import sangria.schema.{ObjectType, ScalarType}

case class LatestExchangeRates(ID: String, date: DateTime, rates: Seq[ExchangeRate])

object LatestExchangeRates {
  implicit val ct: ObjectType[Unit, Currency] = Currency.CurrencyType
  implicit val dt: ScalarType[DateTime] = DateConverter.DateTimeType
  implicit val et: ObjectType[Unit, ExchangeRate] = ExchangeRate.ExchangeRateType

  val LatestExchangeRatesType: ObjectType[Unit, LatestExchangeRates] = deriveObjectType[Unit, LatestExchangeRates]()
}