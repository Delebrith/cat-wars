package logic

import scala.annotation.tailrec

/**
  * Class for game board
  * 
  * @constructor creates board with given field matrix
  * 
  * @param fields Fields matrix
  */
case class Board(fields: Vector[Vector[Field]]) {
  /**
    * @constructor creates board with given size
    * 
    * @param width Game board width
    * @param height Game board height 
    */
  def this(width: Int, height: Int) = this (
    (for (y <- 0 until height) yield
      (for (x <- 0 until width) yield
        Field(Point(x, y), None, None)
      ).toVector
    ).toVector
  )
  
  /**
    * @return Number of columns
    */
  def width(): Int = {
    if (fields.nonEmpty) fields(0).length else 0
  }
  /**
   	* @return Number of rows
   	*/
  def height(): Int = {
    fields.length
  }
  
  /**
    * Calculates number of other players' dots contained in given player's bases
    * 
    * @param player Player whose points are to be returned
    * 
    * @return Current number of given player's points
    */
  def numberOfPoints(player: Player): Int = {
    fields.flatten.count(f => f.dot.isDefined && !f.dot.contains(player) && f.base.contains(player))
  }

  /**
    * Checks for current winner
    * 
    * @return player with highest score or None in case of a draw 
    */
  def winner(): Option[Player] = {
    val points = fields.flatten.filter(f => f.dot.isDefined && f.base.isDefined && f.dot != f.base)
      .groupBy(_.base).mapValues(_.size).toList.sortWith(_._2 > _._2)
    if (points.size == 1)
      points.head._1
    else if (points.isEmpty || points(0)._2 == points(1)._2)
      None
    else
      points.head._1
  }
  
  /**
   	* @return sequence of fields that are not in any base and don't have dot on them
   	*/
  def getEmptyFields: Seq[Field] = {
    fields.flatten.filter(_.canPlaceDot)
  }
  
  /**
    * Checks if board is full
    *  
    * @return true if exists empty field, else false   
    */
  def isBoardFull: Boolean = {
    getEmptyFields.isEmpty
  }
  
  /**
    * Places dot and creates placing player bases if any possible
    * 
    * @param location Point where the dot is being placed
    * @param player Player placing the dot
    * 
    * @return board with placed dot and, if possible, created bases
    * 
    * @throws GameLogicException if location is not within board or field already has dot/base
    * 
    * @note bases are created always if possible and are always created as maximal 
    */
  def placeDot(
      location: Point,
      player: Player): Board = {
    
    if (location.x < 0 || location.y < 0 || location.x >= width() || location.y >= height())
      throw GameLogicException("Location is out of board")
    
    val newFields =
      fields.map(_.map((f: Field) =>
        if (f.location == location) 
          f.placeDot(player) 
        else 
          f))

    val boardBorders = 
      (for (x <- 0 until width) yield List(newFields(0)(x), newFields.last(x)))
      .++(for (y <- 1 until height - 1) yield List(newFields(y)(0), newFields(y).last))
      .flatten
    
    val fieldsNotInBases = fillFields(
      newFields,
      boardBorders,
      (f: Field) => !(f.dot.contains(player) && f.base.isEmpty || f.base.contains(player)))
    
    //get enemy dots that are not in our bases
    val fieldsInBases = newFields
      .view
      .flatten
      .filter(field => field.dot.getOrElse(player) != player && !field.base.contains(player))
      .flatMap(field => fillFields(
          newFields,
          List(field),
          (f: Field) => !fieldsNotInBases.contains(f)))
      .force
    
    Board(
      newFields
        .map(_.map(field => 
          if (fieldsInBases.toIndexedSeq.contains(field))
            Field(field.location, field.dot, Some(player)) 
          else
            field))
      )
  }

  /**
    * Flood fill algorithm implementation
    * 
    * @param fields field matrix to be considered as current, may differ from board's fields field
    * @param startFrom sequence of fields that will be checked if match condition and will be starting points for filling
    * @param condition condition that must be met by field to be filled
    * 
    * @return filled fields 
    */
  private def fillFields(
      fields: Vector[Vector[Field]],
      startFrom: Seq[Field],
      condition: Field => Boolean): Seq[Field] = {
    val filteredStartFrom = startFrom.filter(condition)
    fillFields(fields, filteredStartFrom, Seq[Field](), condition)
  }

  /**
    * Flood fill algorithm implementation
    * 
    * @param fields field matrix to be considered as current, may differ from board's fields field
    * @param runFrom sequence of fields which surrounding are to be checked
    * @param filled fields that already have been filled and checked for possible continuation
    * @param condition condition that must be met by field to be filled
    * 
    * @return filled fields 
    */  
  @tailrec
  private def fillFields(
      fields: Vector[Vector[Field]],
      runFrom: Seq[Field],
      filled: Seq[Field],
      condition: Field => Boolean): Seq[Field] = {
      if (runFrom.isEmpty)
        filled
      else {
        
        val surroundings = (f: Field) => for {
            x <- f.location.x - 1 to f.location.x + 1
            y <- f.location.y - 1 to f.location.y + 1
              if (x != f.location.x || y != f.location.y) &&
                  (x == f.location.x || y == f.location.y) &&
                  x >= 0 && x < width &&
                  y >= 0 && y < height} yield fields(y)(x)

                  
        val nextFrom = runFrom
        .view
        .flatMap(surroundings)
        .filter(f => !filled.contains(f))
        .filter(f => !runFrom.contains(f))
        .filter(condition)
        .distinct
        .force
        
        fillFields(fields, nextFrom, runFrom ++ filled, condition)
      }
  }
  
}

