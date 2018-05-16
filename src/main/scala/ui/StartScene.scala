package ui

import logic.Level
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.paint.Color.BlueViolet
import scalafx.Includes._
import scalafx.geometry.Insets

class StartScene(width: Double, height: Double) extends Scene {
  fill = BlueViolet
  val buttons = Array.ofDim[Button](3)
  buttons(0) = new Button("EASY")
  buttons(0).onAction = handle {
    print("level: " + Level.EASY)
  }
  buttons(1) = new Button("MEDIUM")
  buttons(1).onAction = handle {
    print("level: " + Level.MEDIUM)
  }
  buttons(2) = new Button("HARD")
  buttons(2).onAction = handle {
    print("level: " + Level.HARD)
  }
  for (i <- 0 until buttons.length) {
    buttons(i).setPrefSize(width/3, width/12)
    buttons(i).setPadding(Insets.apply(20))
    buttons(i).setLayoutX(width/3)
    buttons(i).setLayoutY(100 + width/10 * (i+1))
  }
  content = buttons
}
