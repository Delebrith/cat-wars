package logic

object Level extends Enumeration {
  type Level = Value
  val EASY, MEDIUM, HARD = Value
  
  def getLevelDepth(value: Value) : Int = {
    value match {
      case EASY => 3
      case MEDIUM => 4
      case HARD => 5
      case _ => 0
    }
  }
}

