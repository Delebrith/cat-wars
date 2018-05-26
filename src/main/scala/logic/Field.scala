package logic

case class Field (location: Point, dot: Option[Player], base: Option[Player]) {
  def placeDot(player: Player): Field = {
    
    if (dot.isDefined)
      throw GameLogicException("Sir! We can`t place our cat on another one!")
    
    if (base.isDefined)
      throw GameLogicException("Sir! This fields is already taken!")
    
    Field(location, Some(player), None)
  }
  
  def canPlaceDot: Boolean = {
    dot.isEmpty && base.isEmpty
  }
}