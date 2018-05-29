package controller

import logic.{Board, Level, Player, Point}
import app.App.instance
import logic.Level.Level
import scalafx.stage.Stage
import view.{DialogStage, GameScene, StartScene}

import scala.util.Random
import logic.AI
import scalafx.application.Platform

/**
  * Controller ot the game. It manages views, level of difficulty and logic.
  */
object Game {

  /**
    * Contains level of difficulty for currently played game. Value os set for every game separately
    */
  private var level = Level.EASY
  /**
    * Random value for generation of pictures on the tiles
    */
  private val randomSeed = Random.nextInt()

  /**
    * Method generates new board with newly placed dot, then creates view with new board.
    *
    * @param board current state of the game
    * @param location Point with coordinates of newly placed dot
    * @param player player who placeed the dot
    * @param enemy another player
    * @return new state of the game, after dot has been placed
    */
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

  /**
    * Hadler for placing the dot by a player. Method creates an objects of AI with set level of difficulity,
    * generates state of the game after player (human) has placed the dot and then after AI's move.
    *
    * @param board current state of the game
    * @param x x coordinate of newly placed dot
    * @param y y coordinate of newly placed dot
    */
  def placeDot(board: Board, x: Int, y: Int): Unit = {
    val ai = Player(PlayerName.COMPUTER.toString)
    val human = Player(PlayerName.PLAYER.toString)
    
    val afterHuman = placeDot(board, Point(x, y), human, ai)
  
    //it's ugly but it works
    //and for it being ugly javafx & scalafx should be the ones to blame
    //theoretically, this could have been done by Future but scalafx & javafx make it impossible
    //because of thread management
    if (!afterHuman.isBoardFull) {
      instance.stage.scene.value.rootProperty().get()
        .getChildrenUnmodifiable.get(0).asInstanceOf[javafx.scene.Parent]
        .getChildrenUnmodifiable.get(0).setDisable(true)
      new Thread(() => Platform.runLater(
        {
          val aiMove = AI(ai, Level.getLevelDepth(level)).getNextMove(afterHuman, human)
          placeDot(afterHuman, aiMove, ai, human)
        }
        )).start()
    }
  }

  /**
    * Moves application to the start scene
    */
  def restart(): Unit = {
    instance.stage.scene = new StartScene(instance.stage.width.value, instance.stage.height.value)
  }

  /**
    * Starts the game with given level of difficulily by generating and displaying initial state of the game
    * (empty board)
    * @param level level of difficulty
    */
  def start(level: Level) {
    this.level = level
    val player = Player(PlayerName.PLAYER.toString)
    instance.stage.scene = new GameScene(
      new Board(9, 7), instance.stage.width.value, instance.stage.height.value * 0.75, randomSeed, player)
  }

  /**
    * Ends game and displays dialog
    * @param winner Player who has won the battle
    */
  def finish(winner: Option[Player]): Unit = {

    val dialog : Stage = new DialogStage(winner)
    dialog.showAndWait()
  }
}
