package logic

import scala.annotation.tailrec

case class Board(fields: Vector[Vector[Field]]) {
  /*
   * additional constructor that creates empty game board
   */
  def this(width: Int, height: Int) = this (
    (for (y <- 0 until height) yield
      (for (x <- 0 until width) yield
        Field(Point(x, y), None, None)
      ).toVector
    ).toVector
  )
  
  def width(): Int = {
    if (fields.nonEmpty) fields(0).length else 0
  }
  def height(): Int = {
    fields.length
  }
  
  def numberOfPoints(player: Player): Int = {
    fields.flatten.count(f => f.dot.isDefined && !f.dot.contains(player) && f.base.contains(player))
  }

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
  
  def getEmptyFields: Seq[Field] = {
    fields.flatten.filter(_.canPlaceDot)
  }
  
  def isBoardFull: Boolean = {
    getEmptyFields.isEmpty
  }
  
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

  private def fillFields(
      fields: Vector[Vector[Field]],
      startFrom: Seq[Field],
      condition: Field => Boolean): Seq[Field] = {
    val filteredStartFrom = startFrom.filter(condition)
    fillFields(fields, filteredStartFrom, Seq[Field](), condition)
  }
  
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

