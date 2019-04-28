package models

import org.joda.time.DateTime

case class CurrencyPeriod(currency: Currency, from: DateTime, to: DateTime)

case class CurrencyPeriodData(currencyPeriod: CurrencyPeriod, rates: Seq[DailyExchangeRates])

case class DailyExchangeRates(ID: String, date: DateTime, bid: BigDecimal)
