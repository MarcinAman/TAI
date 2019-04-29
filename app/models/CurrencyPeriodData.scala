package models

import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class CurrencyPeriodData(currencyPeriod: CurrencyPeriod, rates: Seq[DailyExchangeRates])

object CurrencyPeriodData {
  implicit val cpt: ObjectType[Unit, CurrencyPeriod] = CurrencyPeriod.CurrencyPeriod
  implicit val rt: ObjectType[Unit, DailyExchangeRates] = DailyExchangeRates.DailyExchangeRatesType

  val CurrencyPeriodDataType: ObjectType[Unit, CurrencyPeriodData] = deriveObjectType[Unit, CurrencyPeriodData]()
}

