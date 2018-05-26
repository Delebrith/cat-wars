package logic

object Level extends Enumeration {
  type Level = Value
  val EASY, MEDIUM, HARD = Value
  
  def getLevelDepth(value: Value) : Int = {
    value match {
      case EASY => 2
      case MEDIUM => 3
      case HARD => 4
      case _ => 0
    }
  }
}

