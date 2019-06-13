package services

import akka.NotUsed
import akka.stream.scaladsl.Source
import javax.inject.Inject
import models.{Currency, CurrencyPeriod, CurrencyPeriodData, ProfitEstimationRequest, ProfitEstimationResponse}
import org.joda.time.{DateTime, Days}
import services.impl.NBPCurrencyService

private case class Line(a: Double, b: Double)

class ProfitEstimationService @Inject() (private val regressionService: CurrencyRateRegressionService, private val NBPService: NBPCurrencyService) {
  def calculateProfit(request: ProfitEstimationRequest): Source[ProfitEstimationResponse, NotUsed] = {

    val duration = Math.max(Days.daysBetween(request.from, request.to).getDays,2)

    val learningData = this.currencyPeriodFromRequest(request, NBPService, duration)
    val regressionData = learningData.map(regressionService.calculateRegression)

    val estimation = regressionData.map(lineFromPoints)

    estimation.map(d => calculateProfit(request, d, DateTime.now minusDays duration))
  }

  private def currencyPeriodFromRequest(request: ProfitEstimationRequest, service: NBPCurrencyService, learningDurationInDays: Int): Source[CurrencyPeriodData, NotUsed] = {
    val (from, to) = (DateTime.now minusDays learningDurationInDays, DateTime.now)

    service.fetchCurrencyDataFromPeriod(CurrencyPeriod(Currency.fromCode(request.code), from, to))
  }

  private def lineFromPoints(points: CurrencyPeriodData): Line = {
    val b = points.rates.head.bid
    val a = points.rates.reverse.head.bid / (points.rates.length -1) - b

    Line(a.doubleValue(), b.doubleValue())
  }

  private def calculateProfit(request: ProfitEstimationRequest, estimatedFunction: Line, zeroDate: DateTime): ProfitEstimationResponse = {
    val beginingOffset = Days.daysBetween(zeroDate, request.from).getDays

    val endOffset = Days.daysBetween(zeroDate, request.to).getDays

    val profitPreTax = (estimatedFunction.a * endOffset - estimatedFunction.a * beginingOffset) * request.amount

    if(profitPreTax < 0 ){
      ProfitEstimationResponse(profitPreTax, profitPreTax)
    } else {
      ProfitEstimationResponse(profitPreTax, profitPreTax * (100 - request.tax) / 100)
    }
  }
}
