package controller

import app.App
import logic.{Board, Level, Player, Point}
import app.App.instance
import logic.Level.Level
import scalafx.scene.control.{Alert, Dialog}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.Region
import scalafx.stage.{Modality, Stage, StageStyle}
import view.{DialogScene, GameScene, StartScene}

import scala.util.Random

object Game {

  private var level = Level.EASY
  private val randomSeed = Random.nextInt()

  def placeDot(board: Board, x: Int, y: Int): Unit = {
    val newBoard = board.placeDot(Point(x, y), Player(PlayerName.PLAYER.toString))
    instance.stage.scene =
      new GameScene(newBoard, instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed)
    //TODO place dot for AI
    if (newBoard.isBoardFull())
      finish(board.winner())
  }

  def restart(): Unit = {
    instance.stage.scene = new StartScene(instance.stage.width.value, instance.stage.height.value)
  }

  def start(level: Level) {
    this.level = level
    instance.stage.scene = new GameScene(
      new Board(3, 2), instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed)
  }

  def finish(winner: Option[Player]): Unit = {

    val opaqueLayer = new Region {
      style_=("-fx-background-color: #00000044;")
      visible_=(false)
    }

    val dialog : Stage = new Stage{
      scene_=(new DialogScene(winner))
      initOwner(App.stage)
      initModality(Modality.WindowModal)
      initStyle(StageStyle.Undecorated)
    }
    dialog.showAndWait()
  }
}
