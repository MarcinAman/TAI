package models
import sangria.macros.derive._
import sangria.schema.ObjectType

case class Currency(currencyName: String, code: String)

object Currency {
  val CurrencyType: ObjectType[Unit, Currency] =
    deriveObjectType[Unit, Currency]()

  def fromCode(currencyCode: String): Currency = Currency("", currencyCode)
}