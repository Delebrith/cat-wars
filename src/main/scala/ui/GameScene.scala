package ui

import logic.{Board, Game}
import scalafx.scene.Scene
import scalafx.scene.paint.Color._

class GameScene(width: Double, height: Double, board: Board) extends Scene {
  fill = Navy
  val boardView = GameView.generateBoardView(board, width, height)
  val scoreView = GameView.generateScoreLabels("Player", 1234, 4321)
  val image  = GameView.generfateOfficerImage(width, height)
  content = boardView ++ scoreView :+ image
}
