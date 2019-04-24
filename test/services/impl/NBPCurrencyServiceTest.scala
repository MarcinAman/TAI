package services.impl

import models.{Currency, CurrencyPeriod}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.mockito.Mockito
import org.scalatest.FlatSpec
import play.api.libs.ws.WSClient
import services.impl.LatestExchangeDTO.dateFormat

class NBPCurrencyServiceTest extends FlatSpec {
  val currency = Currency(currencyName = "Polski zloty", code = "PLN")
  val wsClientMock: WSClient = Mockito.mock(classOf[WSClient])
  val service = new NBPCurrencyService(wsClientMock)

  "splitPeriodByMaxTime" should "handle periods shorter than 92 days" in {
    val startingDate = DateTime.parse("2019-04-24", DateTimeFormat.forPattern(dateFormat))
    val endDate = DateTime.parse("2019-04-26", DateTimeFormat.forPattern(dateFormat))

    val period = CurrencyPeriod(currency, startingDate, endDate)

    val result = service.splitPeriodByMaxTime(period).headOption

    assert(result.isDefined)
    assert(result.get.from.toDate == period.from.toDate)
    assert(result.get.to.toDate == period.to.toDate)
  }

  "splitPeriodByMaxTime" should "handle periods longer than 92 days" in {
    val startingDate = DateTime.parse("2019-04-24", DateTimeFormat.forPattern(dateFormat))
    val endDate = DateTime.parse("2019-08-02", DateTimeFormat.forPattern(dateFormat))

    val period = CurrencyPeriod(currency, startingDate, endDate)

    val result = service.splitPeriodByMaxTime(period)

    val firstExpectedPeriod = CurrencyPeriod(currency, startingDate plusDays 92, endDate)
    val secondExpectedPeriod = CurrencyPeriod(currency, startingDate, startingDate plusDays 92)


    assert(result.size == 2)
    assert(result.head.from.toDate == firstExpectedPeriod.from.toDate)
    assert(result.head.to.toDate == firstExpectedPeriod.to.toDate)

    assert(result(1).from.toDate == secondExpectedPeriod.from.toDate)
    assert(result(1).to.toDate == secondExpectedPeriod.to.toDate)
  }

}
