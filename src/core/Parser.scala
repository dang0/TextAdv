package core

object Parser {
  
  private val REprompt = """>[ \t\n]+(.*)"""r
  private val REcommand = """(
    |go|move
    |get
    |pick up
    |drop
    |put
    |stow
    |inventory|inv
    |held
    |look
    |help)"""r
  private val REflags = """([0-9]+)"""r
  private val REdir = """(
    |n|north
    |s|south
    |e|east
    |w|west)[ \t\n]*.*"""r
  private val REargs = """(\w+)[ \t\n]*(.*)"""r
  private val REqual = """(.*)\b(\w+)\b"""r
  private val REspec = """([ \w]+)[ \t\n]+(
    |in
    |on
    |from
    |to)[ \t\n]+([ \w]+)"""r
  private val REspace = """[ \t\n]+"""r

  def first(str: String): String = str match {
    case REargs(a, _) => a
    case _ => ""
  }
  
  def rest(str: String): String = str match {
    case REargs(_, b) => b
    case _ => ""
  }
  
  def last(str: String): String = str match {
    case REqual(_, c) => c
    case _ => ""
  }

  def quals(str: String): Array[String] = {
    str.split(" ").slice(0, str.split(" ").length-1)
  }
  private def go(cmd: String, args: String): String = args match {
    case REdir(a) => Player.curRoom.exit(a)
    case _ => cmd + " where?"
  }

  private def handleItem(cmd: String, args: String): String = {
    var item = "unknown"
    var tgt = "roomORinv"
    args match {
      case REspec(a,_,b) => item = a; tgt = b
      case REqual(a,b) => item = a+b
      case _ => 
    }
    cmd match {
      case "drop" => Player.itemShift("held", tgt, item)
      case "put" => Player.itemShift("heldORroom", tgt, item)
      case "stow" => Player.itemShift("held", "inv", item)
      case ("get" | "pick up") => Player.itemShift(tgt, "held", item)
      case _ => cmd + " what?"
    }
  }

  private def look(cmd: String, args: String): String = args match {
    //case ("at", REargs(a,b)) => Player.lookAt(a)
    case _ => Player.curRoom.showRoom
  }

  private def command(cmd: String, args: String): String = cmd match {
    case "inv" | "inventory" => Player.listInventory.capitalize
    case "go" | "move" => go(cmd, args)
    case "drop" | "put" | "stow" | "get" | "pick up" => handleItem(cmd, args)
    case "held" => Player.showHeld
    case "look" => look(cmd, args)
    case "help" => "Command list: " + REcommand
    case _ => "I don't understand."
  }

  def read(input: String): String = input match {
      case REprompt(a) => command(first(a), rest(a))
      case _ => "You messed up the input, bruh."
  }
}