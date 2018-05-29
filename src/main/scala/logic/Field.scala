package logic

/**
  * Class for game board's single field
  * 
  * @constructor creates field from given parameters
  * 
  * @param location Field's location 
  * @param dot None if no dot has been placed here, else player who placed dot
  * @param base None if there is no base, else player who owns the base
  */
case class Field (location: Point, dot: Option[Player], base: Option[Player]) {
  /**
    * Places dot of the player on this field
    * 
    * @param player Player that places the dot
    * 
    * @return field with placed dot
    * 
    * @throws GameLogicException if field is already taken (contains dot/base)
    */
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