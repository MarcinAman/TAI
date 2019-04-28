package controllers

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import javax.inject._
import models.{Currency, CurrencyPeriod, TableA}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._
import repositories.PictureRepository
import services.GraphQLService
import services.impl.NBPCurrencyService

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Frontend controller managing all static resource associate routes.
  * @param assets Assets controller reference.
  * @param cc Controller components reference.
  */
@Singleton
class FrontendController @Inject()(
  assets: Assets,
  errorHandler: HttpErrorHandler,
  config: Configuration,
  cc: ControllerComponents,
  graphQLService: GraphQLService,
  pictureRepository: PictureRepository,
  currencyService: NBPCurrencyService
)(implicit val materializer: Materializer) extends AbstractController(cc) {

  def index: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(config.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else index
  }

  def graphQL = Action.async(parse.json)  { request =>
    graphQLService.graphQLEndpoint(request)
  }

  def mongoTest = Action.async {
    pictureRepository.findFirst().head().map(d => Ok(d.toJson()))
  }

  def fetchLatestExchangeRatesA() = Action.async{
    currencyService.fetchLatestExchangeRates(Some(TableA)).runWith(Sink.head).map(e => Ok(e.toString))
  }

  def fetchLatestExchangeRatesB() = Action.async {
    currencyService.fetchLatestExchangeRates().runWith(Sink.head).map(e => Ok(e.toString))
  }

  def fetchCurrencyList() = Action.async {
    currencyService.fetchCurrencyList().runWith(Sink.seq).map(e => Ok(e.toString))
  }

  def fetchCurrencyDataFromPeriod() = Action.async {
    val dateFormat = "yyyy-MM-dd"
    val from = DateTime.parse("2018-01-24", DateTimeFormat.forPattern(dateFormat))
    val endDate = DateTime.parse("2019-01-10", DateTimeFormat.forPattern(dateFormat))

    currencyService.fetchCurrencyDataFromPeriod(CurrencyPeriod(Currency("funt", "gbp"), from, endDate)).runWith(Sink.seq).map(e => Ok(e.toString))
  }
}
