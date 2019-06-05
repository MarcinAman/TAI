package models

import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

import scala.collection.JavaConversions._
import org.mongodb.scala.bson.{BsonString, _}


case class User(
  firstName: String,
  lastName: String,
  email: String,
  dashboardCurrencies: List[String]
)

object User {
  val UserDataType: ObjectType[Unit, User] = deriveObjectType[Unit, User]()

  def serialize(usr: User): Document = {
    import usr._
    Document(
      "firstName" -> BsonString(firstName),
      "lastName" -> BsonString(lastName),
      "email" -> BsonString(email),
      "dashboardCurrencies" -> dashboardCurrencies.map(x => BsonString(x))
    )
  }
  def deserialize(doc: Document) = {
    User(
      doc.getOrElse("firstName","").asString().getValue,
      doc.getOrElse("lastName","").asString().getValue,
      doc.getOrElse("email","").asString().getValue,
      doc.getOrElse("dashboardCurrencies", BsonArray()).asArray().map(_.asString().getValue).toList,
    )
  }

}