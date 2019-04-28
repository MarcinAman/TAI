package services

import akka.NotUsed
import akka.stream.scaladsl.Source
import models.{Currency, CurrencyPeriod, CurrencyPeriodData, CurrencyTable, LatestExchangeRates}

trait CurrencyService {
  def fetchLatestExchangeRates(table: Option[CurrencyTable] = None): Source[LatestExchangeRates, NotUsed]

  def fetchCurrencyList(): Source[Currency, NotUsed]

  def fetchCurrencyDataFromPeriod(period: CurrencyPeriod): Source[CurrencyPeriodData, NotUsed]
}
