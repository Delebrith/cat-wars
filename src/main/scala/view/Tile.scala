package view

import java.util.logging.Logger

import controller.Game
import scalafx.Includes._
import logic.{Board, Field, Player}
import scalafx.geometry.Insets
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent

/**
  * Graphic representation of logic.field
  *
  * @param board structure of fields with current state of the game
  * @param field field of the board to represent by Tile
  * @param size size of the tile in pixels
  * @param randomSeed random number used for random placement of images in the tiles of the board
  */
class Tile (board: Board, field: Field, size: Double, randomSeed: Int) extends Button {

  /**
    * Finds the index of the picture to place on the tile. The index is generated with
    * the randomSeed
    * @param randomSeed
    * @return index of the picture
    */
  private def getSoldierIndex(randomSeed: Int) : Int = {
    val x = field.location.x
    val y = field.location.y
    (((x + 141) * 1141 % (y + 17)) ^ randomSeed).abs % 4 + 1
  }

  /**
    * Prepare an image to display on Player's tiles. Image is chosen from resources
    * with getSoldierImage method
    * @return Image of player's soldier
    */
  private def getPlayerSoldierImage : Image = {
    val path: String = "player_soldier_0" + getSoldierIndex(randomSeed) + ".png"
    new Image(path)
  }

  /**
    * Prepare an image to display on computer's tiles. Image is chosen from resources
    * with getSoldierImage method
    * @return Image of computer's soldier
    */
  private def getEnemySoldierImage : Image = {
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


  if (field.base.contains(Player(PlayerName.COMPUTER.toString))) {
    style_=("-fx-background-color: #FFBBBB")
  } else  if (field.base.contains(Player(PlayerName.PLAYER.toString))) {
    style_=("-fx-background-color: #BBBBFF")
  } else {
    style_=("-fx-background-color: #BBFFBB")
  }

  handleEvent(MouseEvent.MouseReleased)  {
    e: MouseEvent => {
      try {
        Game.placeDot(board, field.location.x, field.location.y)
      } catch {
        case e: logic.GameLogicException => new ExceptionStage(e).showAndWait()
      }
    }
  }
}
