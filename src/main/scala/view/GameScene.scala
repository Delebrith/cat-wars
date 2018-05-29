package view

import logic.{Board, Player}
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

/**
  * Main scene of the game. Contains board, message panel and points counter.
  *
  * @param board structire with current state of the game
  * @param windowWidth width in pixels
  * @param windowHeight height in pixels
  * @param randomSeed random value for generation of different pictures on the tiles
  * @param player Player to make a move in current sitting
  */
class GameScene(board: Board, windowWidth: Double, windowHeight: Double, randomSeed: Int, player: Player) extends Scene {
    fill_=(Color.MintCream)
    stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))
    content = new VBox {
      children = List(new BoardPane(windowWidth, 0.80 * windowHeight, board, randomSeed),
      new InfoPane(windowWidth, 0.20 * windowHeight, board, player))
    }
}
