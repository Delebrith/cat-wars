package view

import java.util.logging
import java.util.logging.Logger

import controller.Game
import scalafx.Includes._
import logic.{Board, Field, Player}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent

import scala.util.Random

class Tile (board: Board, field: Field, size: Double, randomSeed: Int) extends Button {

  val logger : Logger = Logger.getAnonymousLogger

  def getSoldierIndex(randomSeed: Int) : Int = {
    val x = field.location.x
    val y = field.location.y
    (((x + 141) * 1141 % (y + 17)) ^ randomSeed).abs % 4 + 1
  }

  def getPlayerSoldierImage : Image = {
    val path: String = "player_soldier_0" + getSoldierIndex(randomSeed) + ".png"
    new Image(path)
  }

  def getEnemySoldierImage : Image = {
    val path: String = "computer_soldier_0" + getSoldierIndex(randomSeed) + ".png"
    new Image(path)
  }

  prefWidth_=(size)
  prefHeight_=(size)
  padding_=(Insets(0))

  if (field.dot.isDefined) {
    val imageView =
      if (field.dot.contains(Player(PlayerName.COMPUTER.toString)))
        new ImageView(getEnemySoldierImage)
      else
        new ImageView(getPlayerSoldierImage)
    imageView.smooth_=(true)
    imageView.fitHeight_=(size)
    imageView.fitWidth_=(size)
    this.setGraphic(imageView)
  }


  if (field.base == Some(Player(PlayerName.COMPUTER.toString))) {
    style_=("-fx-background-color: #FFBBBB")
  } else  if (field.base == Some(Player(PlayerName.PLAYER.toString))) {
    style_=("-fx-background-color: #BBBBFF")
  } else {
    style_=("-fx-background-color: #BBFFBB")
  }

  handleEvent(MouseEvent.MouseReleased)  {
    e: MouseEvent => {
      try {
        Game.placeDot(board, field.location.x, field.location.y)
      } catch {
        case e: logic.GameLogicException => (new ExceptionStage(e)).showAndWait()
      }
      logger.log(logging.Level.INFO , "Button clicked: " + field.location.x + " " + field.location.y)
    }
  }
}
