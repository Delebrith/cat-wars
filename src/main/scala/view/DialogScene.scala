package view

import java.util.logging

import app.App
import controller.Game
import javafx.stage
import logic.Player
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene
import scalafx.stage.{Stage, Window}

class DialogScene(winner: Option[Player]) extends Scene {

  stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))

  content = new VBox {
    styleClass_=(List("dialog"))
    alignmentInParent_=(Pos.Center)
    val upperBox = new VBox {
      alignmentInParent_=(Pos.Center)
      alignment_=(Pos.Center)
      hgrow_=(Priority.Always)
      vgrow_=(Priority.Always)
      children = List(
        new Label(
          if (winner.isDefined)
            "The winner is " + winner.get.name
          else
            "It's a draw"){
          styleClass_=(List("dialog-label"))
        },
        new Label(
          if (winner.contains(Player(PlayerName.COMPUTER.toString)))
            "LOOSER!"
          else if (winner.contains(Player(PlayerName.PLAYER.toString)))
            "Congratulations!"
          else
            "You couldn't win with the enemy!"){
          styleClass_=(List("dialog-label"))
        })
    }
    val lowerBox = new HBox {
      spacing_=(10)
      alignmentInParent_=(Pos.Center)
      alignment_=(Pos.Center)
      hgrow_=(Priority.Always)
      vgrow_=(Priority.Always)
      children = List(
        new Button("PLAY AGAIN") {
          handleEvent(MouseEvent.MouseReleased)  {
            e: MouseEvent => {
              Game.restart()
              window.value.asInstanceOf[stage.Stage].close()
            }
          }
          styleClass_=(List("menu-button"))
        },
        new Button("EXIT GAME"){
          handleEvent(MouseEvent.MouseReleased)  {
            e: MouseEvent => {
              App.instance.stage.close()
              App.instance.stopApp()
            }
          }
          styleClass_=(List("menu-button"))
        }
      )
    }
    alignmentInParent_=(Pos.Center)
    alignment_=(Pos.Center)
    hgrow_=(Priority.Always)
    vgrow_=(Priority.Always)
    children = List(upperBox, lowerBox)
  }

}
