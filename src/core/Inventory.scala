package core

import scala.collection.mutable.ArrayBuffer

trait Inventory {
  var inventory = new ArrayBuffer[Item]
  
  def itemDesc(i: String): String = {
    val item = find(i)
    if(item < 0) return ""
    inventory(item).desc
  }
  
  def get(i: String): Boolean = {
    val item = find(i)
    if(item < 0) return false
    Player.held = inventory.remove(item)
    true
  }
  
  def find(itemStr: String): Int = {
    var ans = -1
    for(i <- 0 until inventory.length) {
      if(inventory(i).key == Parser.last(itemStr) 
          && Parser.quals(itemStr).isEmpty 
          || inventory(i).qualifiers.containsSlice(Parser.quals(itemStr))) ans = i
    }
    ans
  }
  
  def listInventory() = {
    if(inventory.isEmpty) "nothing"
    else inventory.mkString(", ")
  }
  
}