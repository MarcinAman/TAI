package services

import javax.inject._
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.mvc._
import play.api.mvc.Results._
import repositories.ProductRepository
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.{QueryParser, SyntaxError}
import sangria.marshalling.playJson._

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class GraphQLService @Inject() () {

  def graphQLEndpoint(request: Request[JsValue]): Future[Result] = {
    val query = (request.body \ "query").as[String]

    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").toOption.map {
      case JsString(vars) =>
        if (vars.trim == "" || vars.trim == "null") {
          Json.obj()
        } else {
          Json.parse(vars).as[JsObject]
        }
      case obj: JsObject => obj
      case _ => Json.obj()
    }

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryAst) ⇒
        executeGraphQLQuery(queryAst, operation, variables)

      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) ⇒
        Future.successful(BadRequest(Json.obj("error" -> error.getMessage)))
    }
  }

  private def executeGraphQLQuery(query: Document, op: Option[String], vars: Option[JsObject]): Future[Result] ={
    val executed = vars.map(v =>
      Executor.execute(ProductRepository.schema, query, new ProductRepository, variables = v, operationName = op))
      .getOrElse(
        Executor.execute(ProductRepository.schema, query, new ProductRepository, operationName = op)
      )
    executed.map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
      }
  }

}
