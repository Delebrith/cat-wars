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
import scalafx.application.Platform

object Game {

  private var level = Level.EASY
  private val randomSeed = Random.nextInt()

  def placeDot(board: Board, location: Point, player: Player, enemy: Player): Board = {
    val newBoard = board.placeDot(location, player)

    instance.stage.scene =
      new GameScene(newBoard, instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed,
        if (!newBoard.isBoardFull)
          enemy
        else
          newBoard.winner().getOrElse(player))

    if (newBoard.isBoardFull)
      finish(board.winner())
    
    newBoard
  }
  
  def placeDot(board: Board, x: Int, y: Int): Unit = {
    val ai = Player(PlayerName.COMPUTER.toString)
    val human = Player(PlayerName.PLAYER.toString)
    
    val afterHuman = placeDot(board, Point(x, y), human, ai)
  
    //it's ugly but it works
    //and for it being ugly javafx & scalafx should be the ones to blame
    if (!afterHuman.isBoardFull) {
      instance.stage.scene.value.rootProperty().get()
        .getChildrenUnmodifiable.get(0).asInstanceOf[javafx.scene.Parent]
        .getChildrenUnmodifiable.get(0).setDisable(true)
      new Thread(() => Platform.runLater(
        {
          val aiMove = AI(ai, Level.getLevelDepth(level)).getNextMove(afterHuman, human)
          placeDot(afterHuman, aiMove, ai, human)
        }
        )).start
    }
  }

  def restart(): Unit = {
    instance.stage.scene = new StartScene(instance.stage.width.value, instance.stage.height.value)
  }

  def start(level: Level) {
    this.level = level
    val player = Player(PlayerName.PLAYER.toString)
    instance.stage.scene = new GameScene(
      new Board(9, 7), instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed, player)
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
