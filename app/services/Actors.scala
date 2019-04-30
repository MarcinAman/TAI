package services

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Actors {
  implicit val sys: ActorSystem = ActorSystem("RequestsSystem")
  implicit val mat: Materializer = ActorMaterializer()
  implicit val es: ExecutionContextExecutor = ExecutionContext.global
}
