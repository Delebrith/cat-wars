package view

import logic.Board
import scalafx.geometry.Pos
import scalafx.scene.layout.Pane

class BoardPane(paneWidth: Double, paneHeight: Double, board: Board, randomSeed: Int) extends Pane {

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

  //style_=("-fx-background-color: #DDFFFF")
  styleClass_=(List("board-pane"))
  alignmentInParent_=(Pos.TopCenter)
  prefWidth_=(paneWidth)
  prefHeight_=(paneHeight)
  minHeight_=(paneHeight + 50)
  children = generateBoard(board)

}
