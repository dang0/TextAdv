package core

trait Desc {
  var name = "Undefined name"
  var desc = "Undefined desc"
  
  def key(): String = Parser.last(name)
  def qualifiers(): Array[String] = {
    name.split(" ").slice(0, name.split(" ").length-1)
  }
}