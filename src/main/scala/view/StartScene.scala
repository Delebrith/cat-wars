package view

import java.util.logging.Logger

import controller.Game
import logic.Level
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Priority, VBox}
import scalafx.Includes._
import scalafx.scene.paint.Color
import java.util.logging

import scalafx.geometry.Pos

class StartScene(windowWidth: Double, windowHeight: Double) extends Scene {

  val logger : Logger = Logger.getAnonymousLogger

  def createButton(buttonWidth: Double, buttonHeight: Double, text: String) : Button = {
    val button : Button = new Button(text)
    button.setPrefSize(buttonWidth, buttonHeight)
    button.setVisible(true)
    button.alignment_=(Pos.Center)
    button.alignmentInParent_=(Pos.Center)
    button.styleClass_=(List("menu-button"))
    button
  }

  val vBox : VBox = new VBox {
    children = for (level <- Level.values) yield {
      val button : Button = createButton(windowWidth/2, windowHeight/8, level.toString)

      button.handleEvent(MouseEvent.MouseReleased)  {
        e: MouseEvent => {
          Game.start(level)
          logger.info( "Starting game. Level: " + level.toString)
        }
      }
      button
    }
    vgrow_=(Priority.Always)
    hgrow_=(Priority.Always)
    minWidth_=(windowWidth)
    minHeight_=(windowHeight)
    styleClass_=(List("start-stage"))
    spacing = windowHeight/16
    alignment_=(Pos.Center)
    alignmentInParent_=(Pos.Center)
  }


  //stylesheets = List(getClass.getResource("app/style.css").toExternalForm)
  stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))
  content = vBox
}
