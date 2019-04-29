package models

import sangria.macros.derive._
import sangria.schema.ObjectType

case class ExchangeRate(currency: Currency, mid: BigDecimal)

object ExchangeRate {
  implicit val ct: ObjectType[Unit, Currency] = Currency.CurrencyType

  val ExchangeRateType: ObjectType[Unit, ExchangeRate] = deriveObjectType[Unit, ExchangeRate]()
}
