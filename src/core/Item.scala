package core

class Item (i: String) extends Desc{
  var id = 0
  name = i
  desc = Description(i)
  
  override def toString(): String = {
    name
  }
}

object Description {
  def apply(i: String): String = {
    i match {
      //case "" => return ""
      case "Teleport Stone" => "Warps you to a previously seen room. You see the word \"UNSTABLE\" etched along the edge."
      case "Return Tag" => "This tag is linked to your longing of home."
      case "a stick" => "A useless stick. Maybe."
      case _ => "How the hell did this get here?"
    }
  }
}