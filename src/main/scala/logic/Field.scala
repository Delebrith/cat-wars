package logic

case class Field (location: Point, dot: Option[Player], base: Option[Player]) {
  def placeDot(player: Player): Field = {
    
    if (dot != None)
      throw GameLogicException("Cannot place dot on an existing one")
    
    if (base != None)
      throw GameLogicException("Cannot place dot inside of a base")
    
    Field(location, Some(player), None)
  }
}