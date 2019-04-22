package services.impl

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Json, Reads}

private[impl] case class LatestExchangeDTO(table: String, no: String, effectiveDate: DateTime, rates: Seq[LatestExchangeRatesDTO])

private[impl] object LatestExchangeDTO {
  val dateFormat = "yyyy-MM-dd"

  implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )

  implicit val latestExchangeReads: Reads[LatestExchangeDTO] = Json.reads[LatestExchangeDTO]
}

private[impl] case class LatestExchangeRatesDTO(currency: String, code: String, mid: BigDecimal)

private[impl] object LatestExchangeRatesDTO{
  implicit val latestExchangeRatesReads: Reads[LatestExchangeRatesDTO] = Json.reads[LatestExchangeRatesDTO]
}