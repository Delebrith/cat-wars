package logic

import scala.util.Random

case class AI(player: Player, predictionDepth: Int) {
  def getNextMove(board: Board, enemy: Player): Point = {
    val possibleMoves = Random.shuffle(board.getEmptyFields)
    
    possibleMoves
      .map(f => (f, alphabeta(board.placeDot(f.location, player), 1, enemy, enemy, Int.MinValue, Int.MaxValue)))
      .maxBy(_._2)._1.location
  }
  
  private def alphabeta(board: Board, depth: Int, currentPlayer : Player, enemy: Player, alpha: Int, beta: Int): Int = {
    if (depth == predictionDepth || board.isBoardFull)
      return board.numberOfPoints(player) - board.numberOfPoints(enemy)
            
    val possibleMoves = board.getEmptyFields
    
    if (currentPlayer == player) {
          
      if (beta < alpha)
        return Int.MaxValue
        
      val potentialNewAlpha = possibleMoves
        .map(f => alphabeta(board.placeDot(f.location, player), depth + 1, enemy, enemy, alpha, beta))
        .max
      val newAlpha = if (alpha > potentialNewAlpha) alpha else potentialNewAlpha 
      
      newAlpha
    }
    else {
      if (beta < alpha)
        return Int.MinValue
      
      val potentialNewBeta = possibleMoves
        .map(f => alphabeta(board.placeDot(f.location, enemy), depth + 1, player, enemy, alpha, beta))
        .min
      val newBeta = if (beta < potentialNewBeta) beta else potentialNewBeta
      
      newBeta
    }
  }
  
}