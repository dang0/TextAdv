package core

import swing._
import swing.event._
import Swing._
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Color
import javax.swing.border.LineBorder

object Main extends MainFrame with App {
  centerOnScreen
  resizable = false
  title = Reference.Title
  //preferredSize = new Dimension(400,400)
  
  menuBar = new MenuBar {
    contents += (
      new Menu("File") {
        mnemonic = Key.F
        contents += (
          new MenuItem(new Action("Save") { mnemonic = KeyEvent.VK_S; accelerator = Some(KeyStroke.getKeyStroke("ctrl S")); def apply { println("save") } }),
          new MenuItem(new Action("Load") { mnemonic = KeyEvent.VK_L; accelerator = Some(KeyStroke.getKeyStroke("ctrl L")); def apply { println("load") } }),
          new Separator,
          new MenuItem(new Action("Close") { mnemonic = KeyEvent.VK_C; accelerator = Some(KeyStroke.getKeyStroke("ctrl C")); def apply { closeOperation } }))
      },
      new Menu("Help") {
        mnemonic = Key.H
        contents += (
          new MenuItem(new Action("About") { def apply { println("about") } }))
      })

  }
  
  val mapPane = new BoxPanel(Orientation.Horizontal) {
    preferredSize = new Dimension(400,400)
    border = new LineBorder(Color.BLACK)
    val map = Map
    val compass = Map.room(Player.location._1, Player.location._2)
    contents += map
    //contents += compass
  }
  
  contents = new BoxPanel(Orientation.Vertical) {
     val output = new TextArea("",5,40) { focusable = false; font = Reference.font; wordWrap = true }
     val outputPane = new ScrollPane(output)
     val input = new TextField("> ") { 
       this.formatted("<b>s</b> ")
       columns = 40
       font = Reference.font
       caret.position = 2
       listenTo(mouse.clicks, keys)
       reactions += {
         case KeyPressed(_,Key.Enter,_,_) => Swing.onEDT(submitCmd())
         case e: KeyPressed if(e.key == Key.Numpad8) => Player.curRoom.exit("n"); mapPane.repaint
         case e: KeyPressed if(e.key == Key.Numpad2) => Player.curRoom.exit("s"); mapPane.repaint
         case e: KeyPressed if(e.key == Key.Numpad6) => Player.curRoom.exit("e"); mapPane.repaint
         case e: KeyPressed if(e.key == Key.Numpad4) => Player.curRoom.exit("w"); mapPane.repaint
         case e: KeyPressed if(e.key == Key.BackSpace && caret.position < 3) => e.consume
         case e: KeyTyped => if(text.length() > 50) e.consume 
         case _ => Swing.onEDT(modInput())
         }
       }
     def submitCmd() {
      //println(input.text)
      output.append(input.text + "\n")
      output.append(Parser.read(input.text) + "\n")
      output.selectAll
      mapPane.repaint
      input.text = "> "
    }
     
     def modInput() {
       if(input.text.length() < 2) input.text = "> " + input.text.substring(0)
       else if(input.text.charAt(0) != '>' || input.text.charAt(1) != ' ') input.text = "> " + input.text.substring(2)
       else if(input.caret.position < 2) input.caret.position = 2
     }
     
    contents += (
      mapPane, //Map
      outputPane,
      new Separator,
      input)

  }
  pack
  visible = true
}