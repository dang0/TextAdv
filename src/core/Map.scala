package core

import swing._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.io.FileNotFoundException
import scala.util.Random

object Map extends Component {
  focusable = false
  var rooms = Array.ofDim[Room](50,50)
  var seen = new ArrayBuffer[(Int, Int)]
  var created = new ArrayBuffer[(Int, Int)]
  //rooms(25)(25) = new Room()
  //rooms(24)(25) = new Room()
  mapReader("src/resources/baselayout.m")
  val start = room(0,0)
  
  val end = room(Random.nextInt(50),Random.nextInt(50))
  
  //start.visible = true
  //seen += Pair(0,0)
  //seen += Pair(-1,0)
  //room(0,0).open()
  //room(-1,0).open('s','e')
  
  
  def mapReader(filename: String) {
    var y = -24
    try {
      for (line <- Source.fromFile(filename).getLines()) {
        var x = -24
        if (line.length() > 0 && line.head != '#') {
          for (rm <- line.split(" ")) {
            if (rm != "0") {
              addRoom(x,y)
              rm match {
                case "0" =>
                case "1" => room(x,y).open('n')
                case "2" => room(x,y).open('s')
                case "3" => room(x,y).open('e')
                case "4" => room(x,y).open('w')
                case "5" => room(x,y).open('n', 's')
                case "6" => room(x,y).open('n', 'e')
                case "7" => room(x,y).open('n', 'w')
                case "8" => room(x,y).open('s', 'e')
                case "9" => room(x,y).open('s', 'w')
                case "a" => room(x,y).open('e', 'w')
                case "b" => room(x,y).open('n', 's', 'e')
                case "c" => room(x,y).open('n', 's', 'w')
                case "d" => room(x,y).open('n', 'e', 'w')
                case "e" => room(x,y).open('s', 'e', 'w')
                case "f" => room(x,y).open()
                case "S" => Player.location = (x,y); room(x,y).check
                case e => throw new java.lang.IllegalArgumentException("Unknown argument: %s" format e)
              }
            }
            x += 1
          }
          y += 1
        }
    }} catch {
      case ex: FileNotFoundException => println("Could not find file")
      case ex: java.io.IOException => println("IO error opening file")
      case ex: Throwable => println("Caught exception: %s" format ex)
    }
  }
  
  def room(x: Int, y: Int): Room = {
    if(x < -25 || x > 25 || y < -25 || y > 25) null
    else rooms(x+25)(y+25)
  }
  
  def addRoom(x: Int, y: Int) {
    rooms(x+25)(y+25) = new Room(x,y)
    created += (x -> y)
    //println(x+ " " +y)
  }
  
  override def paintComponent(g: Graphics2D) {
    g.drawOval(187, 187, 6, 6)
    g.setColor(java.awt.Color.CYAN)
    created foreach { loc => 
      val rx = 180 + (loc._1 * 26) - (Player.location._1 * 26)
      val ry = 180 + (loc._2 * -26) + (Player.location._2 * 26)
      g.drawRect(rx, ry, 20, 20)
      if(loc == (0,0)) g.drawString("@", rx+4, ry+14)
      if(room(loc._1,loc._2).n) g.drawRect(rx+7, ry-6, 6, 6)
      if(room(loc._1,loc._2).s) g.drawRect(rx+7, ry+20, 6, 6)
      if(room(loc._1,loc._2).e) g.drawRect(rx+20, ry+7, 6, 6)
      if(room(loc._1,loc._2).w) g.drawRect(rx-6, ry+7, 6, 6)
      
    }
    g.setColor(java.awt.Color.BLACK)
    seen foreach { loc => 
      var rx = 180 + (loc._1 * 26) - (Player.location._1 * 26)
      var ry = 180 + (loc._2 * -26) + (Player.location._2 * 26)
      g.drawRect(rx, ry, 20, 20)
      if(room(loc._1,loc._2).n) g.drawRect(rx+7, ry-6, 6, 6)
      if(room(loc._1,loc._2).s) g.drawRect(rx+7, ry+20, 6, 6)
      if(room(loc._1,loc._2).e) g.drawRect(rx+20, ry+7, 6, 6)
      if(room(loc._1,loc._2).w) g.drawRect(rx-6, ry+7, 6, 6)
      
      if(rx+4 > 390) rx = 380
      if(ry+14 > 390) ry = 380
      if(rx+4 < 0) rx = 0
      if(ry+14 < 0) ry = 0
      if(loc == (0,0)) g.drawString("@", rx+4, ry+14)
    }
  }
}