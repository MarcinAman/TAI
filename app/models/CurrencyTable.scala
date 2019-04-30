package models

sealed trait CurrencyTable {
  def code: String
}

case object TableA extends CurrencyTable {
  def code = "A"
}
case object TableB extends CurrencyTable {
  def code = "B"
}
case object TableC extends CurrencyTable {
  def code = "C"
}
