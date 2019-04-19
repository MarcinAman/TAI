package repositories

import models.Product
import sangria.schema._

class ProductRepository {
  private val Products = List(
    Product(1, "Cheesecake", "Tasty"),
    Product(2, "Health Potion", "+50 HP"))

  def product(id: Int): Option[Product] =
    Products find (_.id == id)

  def products: List[Product] = Products


}
object ProductRepository{
  val Id = Argument("id", IntType)

  val QueryType = ObjectType("Query", fields[ProductRepository, Unit](

    Field("product", OptionType(Product.ProductType),
      description = Some("Returns a product with specific `id`."),
      arguments = Id :: Nil,
      resolve = c => c.ctx.product(c arg Id)),

    Field("products", ListType(Product.ProductType),
      description = Some("Returns a list of all available products."),
      resolve = _.ctx.products)

  ))

  val schema = Schema(QueryType)

}
