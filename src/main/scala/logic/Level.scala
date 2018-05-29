package logic

/**
  * Enumeration of game difficulty levels
  * 
  * @note calculations for MEDIUM and HARD may take some time, minutes or more for game beginning 
  */
object Level extends Enumeration {
  type Level = Value
  val EASY, MEDIUM, HARD = Value
  
  /**
    * Gets AI prediction analysis depth for given difficulty level
    *  
    * @param value Difficulty level
    * 
    * @return depth for given difficulty level
    */
  def getLevelDepth(value: Value) : Int = {
    value match {
      case EASY => 2
      case MEDIUM => 3
      case HARD => 4
      case _ => 0
    }
  }
}

