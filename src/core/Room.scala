package core

import scala.util.Random

class Room (x: Int, y: Int) extends Desc with Inventory {
  var visible = false
  var n,s,e,w: Boolean = _
  var id = 0
  var location = (x,y)
  name = ""
  desc = ""

    
  def showRoom(): String = {
    name + "\n" +
    desc + "\n" +
    "You see: " + listInventory + "." + "\n" +
    "Exits: " + exits + "."
  }
  def open() {
   n = true; s = true; e = true; w = true
   openAdjRooms
  }
  def open(args: Char*) {
    for (arg <- args)
      arg match {
        case 'n' => if(location._2 < 23) n = true
        case 's' => if(location._2 > -23) s = true
        case 'e' => if(location._1 < 23) e = true
        case 'w' => if(location._1 > -23) w = true
        case _ =>
      }
    //openAdjRooms
  }
  def randOpen() {
    var bail = 0
    var i = 4
    while (i < 7) {
      Random.nextInt(i) match {
        case 0 => if(!n && (nRoom == null || !nRoom.visible || nRoom.s)) {i += 1; open('n')} else bail += 1
        case 1 => if(!s && (sRoom == null || !sRoom.visible || sRoom.n)) {i += 1; open('s')} else bail += 1
        case 2 => if(!e && (eRoom == null || !eRoom.visible || eRoom.w)) {i += 1; open('e')} else bail += 1
        case 3 => if(!w && (wRoom == null || !wRoom.visible || wRoom.e)) {i += 1; open('w')} else bail += 1
        case _ => i += 1
      }
      if(bail > 10) {i += 1; bail = 0}
    }
    openAdjRooms
  }
  
  def openAdjRooms() {
    if(n) {
      if(nRoom == null) Map.addRoom(location._1,location._2+1)
      nRoom.open('s')
    }
    if(s) {
      if(sRoom == null) Map.addRoom(location._1,location._2-1)
      sRoom.open('n')
    }
    if(e) {
      if(eRoom == null) Map.addRoom(location._1+1,location._2)
      eRoom.open('w')
    }
    if(w) {
      if(wRoom == null) Map.addRoom(location._1-1,location._2)
      wRoom.open('e')
    }
  }
  
  def nRoom(): Room = Map.room(location._1,location._2+1)
  def sRoom(): Room = Map.room(location._1,location._2-1)
  def eRoom(): Room = Map.room(location._1+1,location._2)
  def wRoom(): Room = Map.room(location._1-1,location._2)
  
  def exits(): String = {
    var ret = if(n) "north" else ""
    if(s) ret += (if(!ret.isEmpty()) ", " else "") + "south"
    if(e) ret += (if(!ret.isEmpty()) ", " else "") + "east"
    if(w) ret += (if(!ret.isEmpty()) ", " else "") + "west"
    ret
  }
  
  def check() {
    if(!Map.seen.exists(Player.location == _)) {
      //Player.curRoom = new Room(Player.location._1, Player.location._2)
      Player.curRoom.randOpen
      Map.seen += Player.location
      Player.curRoom.visible = true
    }
    if(Player.curRoom == Map.end) {
      
    }
  }
  
  def exit(d: String): String = {
    d match {
      case "n" | "north" if n => Player.move("n"); check; "You go north."
      case "s" | "south" if s => Player.move("s"); check; "You go south."
      case "e" | "east" if e => Player.move("e"); check; "You go east."
      case "w" | "west" if w => Player.move("w"); check; "You go west."
      case "n" | "north"| "s" | "south" | "e" | "east" | "w" | "west" => "Can't walk through walls."
      case _ => "huh?"
    }
  }
}