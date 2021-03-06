package repositories

import com.google.inject.Inject
import models.User
import org.mongodb.scala.{Completed, Observer}
import services.DatabaseService
import org.mongodb.scala.bson._
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepository @Inject() (databaseService: DatabaseService) {
  val collection = databaseService.database.getCollection("users")

  def find(email: String): Future[Seq[User]] = {
    collection.find(Document("email" -> BsonString(email))).collect().toFutureOption().flatMap( f =>
      f.fold(
        Future.failed[Seq[User]](new Exception("Database find failed"))
      )((x: Seq[Document])=>
        Future.successful(x.toList.map(doc => User.deserialize(doc)))
      )
    )
  }

  def save(user: User): Unit = {
    collection.insertOne(User.serialize(user)).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")
      override def onError(e: Throwable): Unit = throw e
      override def onComplete(): Unit = println("onComplete")
    })
  }

  def delete(email: String): Unit = {
    collection.deleteOne(Document("email" -> BsonString(email))).subscribe(new Observer[DeleteResult] {
      override def onError(e: Throwable): Unit = throw e
      override def onComplete(): Unit = println("onComplete")
      override def onNext(result: DeleteResult): Unit = println(s"onNext: $result")
    })
  }
}
