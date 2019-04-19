package models
import sangria.schema._

trait Identifiable {
  def id: Int
}

object Identifiable{
  val IdentifiableType = InterfaceType(
    "Identifiable",
    "Entity that can be identified",
    fields[Unit, Identifiable](
      Field("id", IntType, resolve = _.value.id)))
}