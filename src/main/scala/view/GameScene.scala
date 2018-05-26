package view

import logic.{Board, Player}
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

class GameScene(board: Board, windowWidth: Double, windowHeight: Double, randomSeed: Int, player: Player) extends Scene {
    fill_=(Color.MintCream)
    stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))
    content = new VBox {
      children = List(new BoardPane(windowWidth, 0.80 * windowHeight, board, randomSeed),
      new InfoPane(windowWidth, 0.20 * windowHeight, board, player))
    }
    
    def disable = {
      content.forEach(_.setDisable(true))
    }
}
