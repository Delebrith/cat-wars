package view

import controller.Game
import logic.Level
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Priority, VBox}
import scalafx.Includes._

import scalafx.geometry.Pos

/**
  * Start scene of the game with menu. here players chooses the level of difficulity
  *
  * @param windowWidth width in pixels
  * @param windowHeight height in pixels
  */
class StartScene(windowWidth: Double, windowHeight: Double) extends Scene {

  /**
    * Method generating button of given width, height and text in it. Style of the button
    * is defined in class menu-button in css file.
    *
    * @param buttonWidth width in pixels
    * @param buttonHeight height in pixles
    * @param text string to place on the button
    * @return styled Button with given properties
    */
  private def createButton(buttonWidth: Double, buttonHeight: Double, text: String) : Button = {
    val button : Button = new Button(text)
    button.setPrefSize(buttonWidth, buttonHeight)
    button.setVisible(true)
    button.alignment_=(Pos.Center)
    button.alignmentInParent_=(Pos.Center)
    button.styleClass_=(List("menu-button"))
    button
  }

  private val vBox : VBox = new VBox {
    children = for (level <- Level.values) yield {
      val button : Button = createButton(windowWidth/2, windowHeight/8, level.toString)

      button.handleEvent(MouseEvent.MouseReleased)  {
        e: MouseEvent => {
          Game.start(level)
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

  stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))
  content = vBox
}
