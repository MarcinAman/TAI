package models

import org.joda.time.DateTime
import sangria.macros.derive._
import sangria.schema.{ObjectType, ScalarType}

case class DailyExchangeRates(date: DateTime, bid: BigDecimal)

object DailyExchangeRates {
  implicit val dt: ScalarType[DateTime] = DateConverter.DateTimeType

  val DailyExchangeRatesType: ObjectType[Unit, DailyExchangeRates] = deriveObjectType[Unit, DailyExchangeRates]()
}