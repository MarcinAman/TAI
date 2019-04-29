package models

import org.joda.time.DateTime
import sangria.macros.derive.deriveObjectType
import sangria.schema.{ObjectType, ScalarType}

case class CurrencyPeriod(currency: Currency, from: DateTime, to: DateTime)

object CurrencyPeriod {
  implicit val dt: ScalarType[DateTime] = DateConverter.DateTimeType
  implicit val ct: ObjectType[Unit, Currency] = Currency.CurrencyType

  val CurrencyPeriod: ObjectType[Unit, CurrencyPeriod] = deriveObjectType[Unit, CurrencyPeriod]()
}

