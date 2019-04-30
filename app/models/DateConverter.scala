package models

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import sangria.ast.StringValue
import sangria.marshalling.DateSupport
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object DateConverter {
  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  def parseDate(s: String) = Try(new DateTime(s, DateTimeZone.UTC)) match {
    case Success(date) ⇒ Right(date)
    case Failure(_) ⇒ Left(DateCoercionViolation)
  }

  val DateTimeType = ScalarType[DateTime]("DateTime",
    coerceOutput = (d, caps) ⇒
      if (caps.contains(DateSupport)) d.toDate
      else ISODateTimeFormat.dateTime().print(d),
    coerceUserInput = {
      case s: String ⇒ parseDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    },
    coerceInput = {
      case StringValue(s,_,_,_,_) ⇒ parseDate(s)
      case _ ⇒ Left(DateCoercionViolation)
    })
}
