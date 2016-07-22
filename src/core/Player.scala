package core

object Player extends Inventory{
  var location = (0,0)
  var held: Item = null
  inventory += new Item("a crazy sweet stick")
  
  
  def holding(): Boolean = held.isInstanceOf[Item]
  
  def showHeld(): String = {
    if(!held.isInstanceOf[Item]) "You find your hands are free of any burden."
    else "You are holding " + held.name + "."
  }
  
  def itemShift(src: String, tgt: String, item: String): String = {
    var itemName = "something"
    src -> tgt match {
      // drop
      case ("held", "room") => {
        if(holding) {
          itemName = Player.held.name
          Player.curRoom.inventory += Player.held
          Player.held = null
          "You drop " + itemName + " to the floor."
        }
        else "You're not holding anything."
      }
      // stow
      case ("held", "inv") => {
        if(holding) {
          itemName = Player.held.name
          Player.inventory += Player.held
          Player.held = null
          "You put away " + itemName + " in your inventory."
        }
        else "You're not holding anything."
      }
      // pick up / get
      case ("room", "held") => {
        if(!holding) {
         if(Player.curRoom.get(item)) "You picked up " + Player.held.name + "."
         else "You couldn't find " + item +"."
        }
        else "You've got your hands full!"
      }
      // get
      case ("roomORinv", "held") => {
        if(!holding) {
         if(Player.curRoom.get(item)) "You picked up " + Player.held.name + "."
         else if(Player.get(item)) "You remove " + Player.held.name + " from your inventory."
         else "You couldn't find " + item +"."
        }
        else "You've got your hands full!"
      }
      case ("heldORroom", "") => {
        
      }; ""
      case _ => "Player.itemShift error: " +src+" "+tgt 
    }
  }
  
  def lookAt(target: String): String = {
    if(holding && target == held.key) held.desc
    else if(Player.curRoom.itemDesc(target) != "") Player.curRoom.itemDesc(target)
    else "Couldn't find what you're referring to."
  }
  
  def curRoom = Map.room(location._1,location._2)
  
  def curRoom_=(r: Room) = {
    Map.rooms(location._1+25)(location._2+25) = r
  }
  
  def move(d: String) {
    d match {
      case "n" => move(Player.location._1, Player.location._2+1)
      case "s" => move(Player.location._1, Player.location._2-1)
      case "e" => move(Player.location._1+1, Player.location._2)
      case "w" => move(Player.location._1-1, Player.location._2)
      case _ =>
    }
  }
  
  private def move(x: Int, y: Int) {
    location = (x, y)
  }
}