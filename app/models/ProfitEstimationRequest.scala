package models

import org.joda.time.DateTime
import sangria.macros.derive.deriveObjectType
import sangria.schema.{ObjectType, ScalarType}

case class ProfitEstimationRequest (code: String, from: DateTime, to: DateTime, tax: Int, amount: Int)

object ProfitEstimationRequest {
  implicit val dt: ScalarType[DateTime] = DateConverter.DateTimeType

  val ProfitEstimationRequestDataType: ObjectType[Unit, ProfitEstimationRequest] = deriveObjectType[Unit, ProfitEstimationRequest]()
}
