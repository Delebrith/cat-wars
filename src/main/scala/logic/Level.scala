package logic

object Level extends Enumeration {
  type Level = Value
  val EASY, MEDIUM, HARD = Value
  
  def getLevelDepth(value: Value) : Int = {
    value match {
      case EASY => 4
      case MEDIUM => 5
      case HARD => 6
      case _ => 0
    }
  }
}

