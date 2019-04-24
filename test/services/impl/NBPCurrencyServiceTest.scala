package services.impl

import models.{Currency, CurrencyPeriod}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeComparator}
import org.mockito.Mockito
import org.scalatest.FlatSpec
import play.api.libs.ws.WSClient
import services.impl.LatestExchangeDTO.dateFormat

class NBPCurrencyServiceTest extends FlatSpec {
  val currency = Currency(currencyName = "Polski zloty", code = "PLN")
  val wsClientMock: WSClient = Mockito.mock(classOf[WSClient])
  val service = new NBPCurrencyService(wsClientMock)

  "splitPeriodByMaxTime" should "handle periods shorter than 92 days" in {
    val period = CurrencyPeriod(currency, DateTime.now, DateTime.now plusDays 2)

    val result = service.splitPeriodByMaxTime(period).headOption

    assert(result.isDefined)
    assert(DateTimeComparator.getDateOnlyInstance.compare(result.get.to, period.to) == 0)
    assert(DateTimeComparator.getDateOnlyInstance.compare(result.get.from, period.from) == 0)
  }

  "splitPeriodByMaxTime" should "handle periods longer than 92 days" in {
    val startingDate = DateTime.parse("2019-01-01", DateTimeFormat.forPattern(dateFormat))
    val endDate = DateTime.parse("2019-08-02", DateTimeFormat.forPattern(dateFormat))

    val period = CurrencyPeriod(currency, startingDate, endDate)

    val result = service.splitPeriodByMaxTime(period)

    val firstExpectedPeriod = CurrencyPeriod(currency, startingDate plusDays 93, endDate)
    val secondExpectedPeriod = CurrencyPeriod(currency, startingDate, startingDate plusDays 92)

    assert(result == Seq(firstExpectedPeriod, secondExpectedPeriod))
  }

}
