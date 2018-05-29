package view

import logic.Board
import scalafx.geometry.Pos
import scalafx.scene.layout.Pane

/**
  * Pane includiing an array of tiles. Each tile corresponds one field in the board.
  *
  * @param paneWidth width in pixels
  * @param paneHeight height in pixels
  * @param board current state of the same, with fields taken by players or free
  * @param randomSeed random value used for generating pictures on the board's tiles
  *                   in random order
  */
class BoardPane(paneWidth: Double, paneHeight: Double, board: Board, randomSeed: Int) extends Pane {

  /**
    * Method creates a list of tiles (buttons). It sets size and position of each tile.
    *
    * @param board current state of all the fields
    * @return List of tiles to display in the pane
    */
  def generateBoard(board: Board) : List[Tile] = {
    val columns = board.width()
    val rows = board.height()
    val tileSize = (paneHeight - 50) / rows
    val xOffset = (paneWidth - columns * (tileSize + 5)) / 2
    val yOffset = 25
    val fields = Array.ofDim[Tile](rows, columns)
    for (i <- fields.indices; j <- fields(i).indices) {
      fields(i)(j) = new Tile(board, board.fields(i)(j), tileSize, randomSeed)
      fields(i)(j).setLayoutX(xOffset + (fields(i)(j).getPrefWidth + 5) * j)
      fields(i)(j).setLayoutY(yOffset + (fields(i)(j).getPrefHeight + 5) * i)
    }
    fields.flatten.toList
  }

  styleClass_=(List("board-pane"))
  alignmentInParent_=(Pos.TopCenter)
  prefWidth_=(paneWidth)
  prefHeight_=(paneHeight)
  minHeight_=(paneHeight + 50)
  children = generateBoard(board)

}
