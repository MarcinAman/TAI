package services

import javax.inject._
import org.mongodb.scala._

@Singleton
class DatabaseService @Inject() () {
  // To directly connect to the default server localhost on port 27017
  val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("TAIdb")
}