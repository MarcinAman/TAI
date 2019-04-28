package models

import org.joda.time.DateTime

case class LatestExchangeRates(ID: String, date: DateTime, rates: Seq[ExchangeRate])

case class ExchangeRate(currency: Currency, mid: BigDecimal)