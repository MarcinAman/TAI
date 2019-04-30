package controllers

import akka.stream.Materializer
import javax.inject._
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._
import services.{CurrencyService, GraphQLService}

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
  currencyService: CurrencyService
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
}
