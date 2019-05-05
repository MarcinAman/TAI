package controllers

import akka.stream.Materializer
import javax.inject._
import models.{Currency, CurrencyPeriod, CurrencyPeriodData, DailyExchangeRates}
import org.joda.time.DateTime
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._
import services.{CurrencyRateRegressionService, GraphQLService}

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
  currencyRateRegressionService: CurrencyRateRegressionService
)(implicit val materializer: Materializer) extends AbstractController(cc) {

  def index: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(config.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else index
  }

  def graphQL = Action.async(parse.json)  { request =>
    val x = currencyRateRegressionService.calculateRegression(
      CurrencyPeriodData(
        CurrencyPeriod(
          Currency("PLN", "PLN"),
          DateTime.now().minusDays(4),
          DateTime.now()
        ),
        Seq(
          DailyExchangeRates(
            DateTime.now().minusDays(4),
            BigDecimal(4)
          ),
          DailyExchangeRates(
            DateTime.now().minusDays(3),
            BigDecimal(3)
          ),
          DailyExchangeRates(
            DateTime.now().minusDays(2),
            BigDecimal(2)
          ),
          DailyExchangeRates(
            DateTime.now().minusDays(1),
            BigDecimal(1)
          ),
          DailyExchangeRates(
            DateTime.now().minusDays(0),
            BigDecimal(0)
          )
        )
      )
    )
    print(x)

    graphQLService.graphQLEndpoint(request)
  }
}
