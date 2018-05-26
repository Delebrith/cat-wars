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
import logic.AI

object Game {

  private var level = Level.EASY
  private val randomSeed = Random.nextInt()

  def placeDot(board: Board, location: Point, player: Player): Board = {
    val newBoard = board.placeDot(location, player)
    instance.stage.scene =
      new GameScene(newBoard, instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed)
    
    if (newBoard.isBoardFull)
      finish(board.winner())
    
    newBoard
  }
  
  def placeDot(board: Board, x: Int, y: Int): Unit = {
    val ai = Player(PlayerName.COMPUTER.toString)
    val human = Player(PlayerName.PLAYER.toString)
    
    val afterHuman = placeDot(board, Point(x, y), human)
    
    val aiMove = AI(ai, Level.getLevelDepth(level)).getNextMove(afterHuman, human)
    placeDot(afterHuman, aiMove, ai)
  }

  def restart(): Unit = {
    instance.stage.scene = new StartScene(instance.stage.width.value, instance.stage.height.value)
  }

  def start(level: Level) {
    this.level = level
    instance.stage.scene = new GameScene(
      new Board(4, 3), instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed)
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
