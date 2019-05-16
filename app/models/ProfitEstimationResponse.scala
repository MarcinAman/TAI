package models

import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class ProfitEstimationResponse (profitPreTax: Double, profitPostTax: Double)

object ProfitEstimationResponse {
  val ProfitEstimationResponseDataType: ObjectType[Unit, ProfitEstimationResponse] = deriveObjectType[Unit, ProfitEstimationResponse]()
}