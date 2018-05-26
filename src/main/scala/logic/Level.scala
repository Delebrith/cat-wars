package logic

object Level extends Enumeration {
  type Level = Value
  val EASY, MEDIUM, HARD = Value
  
  def getLevelDepth(value: Value) : Int = {
    value match {
      case EASY => 6
      case MEDIUM => 8
      case HARD => 10
      case _ => 0
    }
  }
}

