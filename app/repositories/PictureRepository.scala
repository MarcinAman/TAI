package repositories

import javax.inject.Inject
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.mutable.Document
import services.DatabaseService

class PictureRepository @Inject() (databaseService: DatabaseService){
  private val pictureCollection: MongoCollection[Document] = databaseService.database.getCollection("pictures")

  def findFirst() = {
    pictureCollection.find().first()
  }
}
