package models

import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class ProfitEstimationResponse (profitPreTax: BigDecimal, profitPostTax: BigDecimal)

object ProfitEstimationResponse {
  val ProfitEstimationResponseReads: ObjectType[Unit, ProfitEstimationResponse] = deriveObjectType[Unit, ProfitEstimationResponse]()
}