package controllers

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import javax.inject._
import models.TableA
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{Completed, Observer}
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
}