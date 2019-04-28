package services.impl

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Json, Reads}

private[impl] object DateDTO {
  val dateFormat = "yyyy-MM-dd"

  implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )
}

private[impl] case class LatestExchangeDTO(
                                            table: String,
                                            no: String,
                                            effectiveDate: DateTime,
                                            rates: Seq[LatestExchangeRatesDTO]
                                          )

private[impl] object LatestExchangeDTO {
  implicit val dateReads: Reads[DateTime] = DateDTO.jodaDateReads

  implicit val latestExchangeReads: Reads[LatestExchangeDTO] = Json.reads[LatestExchangeDTO]
}

private[impl] case class LatestExchangeRatesDTO(
                                                 currency: String,
                                                 code: String,
                                                 mid: BigDecimal
                                               )

private[impl] object LatestExchangeRatesDTO {
  implicit val latestExchangeRatesReads: Reads[LatestExchangeRatesDTO] = Json.reads[LatestExchangeRatesDTO]
}

private[impl] case class CurrencyPeriodDataDTO(
                                                table: String,
                                                currency: String,
                                                code: String,
                                                rates: Seq[CurrencyPeriodRatesDTO]
                                              )

private[impl] object CurrencyPeriodDataDTO {
  implicit val currencyPeriodDataDTOReads: Reads[CurrencyPeriodDataDTO] = Json.reads[CurrencyPeriodDataDTO]
}

private[impl] case class CurrencyPeriodRatesDTO(
                                                 no: String,
                                                 effectiveDate: DateTime,
                                                 mid: BigDecimal
                                               )

private[impl] object CurrencyPeriodRatesDTO {
  implicit val dateReads: Reads[DateTime] = DateDTO.jodaDateReads

  implicit val currencyPeriodRatesDTOReads: Reads[CurrencyPeriodRatesDTO] = Json.reads[CurrencyPeriodRatesDTO]
}
