package logic

import scala.util.Random

/** 
  * A class for minmax algorithm that controls specific player
  * 
  * @constructor creates new AI object that controls given player and analyzes up to predictionDepth moves
  * 
  * @param player The player controlled by AI
  * @param predictionDepth The number of future moves analyzed by AI
  * 
  * @note predicitonDepth should be below 4 because of the worst-case-scenario exponential complexity
  */
case class AI(player: Player, predictionDepth: Int) {
  /**
    * Analyzes possible next moves and selects the best possible
    * 
    * @param board The game board
    * @param enemy The enemy - possibly human-controlled player
    * 
    * @return Returns a move that has best possible outcome according to algorithm, if multiple available selects random
    */
  def getNextMove(board: Board, enemy: Player): Point = {
    val possibleMoves = Random.shuffle(board.getEmptyFields)
    
    possibleMoves
      .map(f => (f, alphabeta(board.placeDot(f.location, player), 1, enemy, enemy, Int.MinValue, Int.MaxValue)))
      .maxBy(_._2)._1.location
  }
  
  /**
   	* Minmax with alpha-beta pruning
   	* 
   	* @param board The game board to analyze
   	* @param depth Number of already made moves during this prediction
   	* @param currentPlayer Player whose move will be now analyzed
   	* @param enemy The enemy - possibly human-controlled player
   	* @param alpha Minimum score that AI player is assured to achieve, according to previous predictions
   	* @param beta Maximum score that enemy player is assured to achieve, according to previous predictions
   	* 
   	* @return best expected score for AI player if this prediction path is chosen 
   	*/
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