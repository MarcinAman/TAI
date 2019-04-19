package models

import sangria.macros.derive._
import models.Picture._

case class Product(id: Int, name: String, description: String) extends Identifiable {
  def picture(size: Int): Picture =
    Picture(width = size, height = size, url = Some(s"//cdn.com/$size/$id.jpg"))
}
object Product{
  val ProductType =
    deriveObjectType[Unit, Product](
      Interfaces(Identifiable.IdentifiableType),
      IncludeMethods("picture"))
}
