package services

import javax.inject.Inject
import models.{ProfitEstimationRequest, ProfitEstimationResponse}

class ProfitEstimationService @Inject() (private val regressionService: CurrencyRateRegressionService) {
  def calculateProfit(request: ProfitEstimationRequest): ProfitEstimationResponse = ???
}
