package logic

import scala.util.Random

case class AI(player: Player, predictionDepth: Int) {
  def getNextMove(board: Board, enemy: Player): Point = {
    val possibleMoves = Random.shuffle(board.getEmptyFields)
    
    possibleMoves.map(f => (f, minmax(board.placeDot(f.location, player), 1, enemy, enemy))).maxBy(_._2)._1.location
  }
  
  private def minmax(board: Board, depth: Int, currentPlayer : Player, enemy: Player): Int = {
    if (depth == predictionDepth || board.isBoardFull)
      board.numberOfPoints(player) - board.numberOfPoints(enemy)
      
    val possibleMoves = board.getEmptyFields
    
    if (currentPlayer == player) {
      possibleMoves.map(f => minmax(board.placeDot(f.location, player), depth + 1, enemy, enemy)).max
    }
    else {
      possibleMoves.map(f => minmax(board.placeDot(f.location, enemy), depth + 1, player, enemy)).min
    }
  }
}