package logic

import scala.annotation.tailrec

case class Board(fields: Vector[Vector[Field]]) {
  /*
   * additional constructor that creates empty game board
   */
  def this(width: Int, height: Int) = this (
    (for (x <- (0 until width)) yield
      (for (y <- (0 until height)) yield
        Field(Point(x, y), None, None)
      ).toVector
    ).toVector
  )
  
  def width(): Int = {
    fields.length;
  }
  def height(): Int = {
    if (fields.length > 0) fields(0).length else 0;
  }
  
  def placeDot(
      location: Point,
      player: Player): Board = {
    val fieldsWithPlacedDot =
      fields.map(_.map((f: Field) =>
        if (f.location == location) 
          Field(location, Some(player), f.base) 
        else 
          f))

    
    val boardBorders = 
      (for (x <- (0 until width())) yield List(fieldsWithPlacedDot(x)(0), fieldsWithPlacedDot(x).last))
      .++(for (y <- (1 until height() - 1)) yield List(fieldsWithPlacedDot(0)(y), fieldsWithPlacedDot(0)(y-1)))
      .flatten
    
    val fieldsNotInBases = fillFields(
      fieldsWithPlacedDot,
      boardBorders,
      (f: Field) => f.dot == player && f.base.getOrElse(player) == player, false)
    
    //get enemy dots that are not in our bases
    val fieldsInBases = fieldsWithPlacedDot
      .flatten
      .filter(field => field.dot.getOrElse(player) != player && field.base.getOrElse(player) != player)
      .flatMap(field => fillFields(
          fieldsWithPlacedDot,
          List(field),
          (f: Field) => fieldsNotInBases.contains(f),
          true))
    
    val newFields = fieldsWithPlacedDot
      .map(_.map(field => 
        if (fieldsInBases.contains(field))
          Field(field.location, field.dot, Some(player)) 
        else
          field))
    
    Board(newFields)
  }
  
  private def fillFields(
      fields: Vector[Vector[Field]],
      startFrom: Seq[Field],
      condition: Field => Boolean,
      allowThroughCorners: Boolean): Seq[Field] = {
    val filteredStartFrom = startFrom.filter(condition)
    fillFields(fields, startFrom, Seq[Field](), condition, allowThroughCorners)
  }
  
  @tailrec
  private def fillFields(
      fields: Vector[Vector[Field]],
      runFrom: Seq[Field],
      filled: Seq[Field],
      condition: Field => Boolean,
      allowThroughCorners: Boolean): Seq[Field] = {
      if (runFrom.length == 0) 
        runFrom
      else {
        
        val surroundings = (f: Field) => for {
            x <- (f.location.x - 1 to f.location.x + 1)
            y <- (f.location.y - 1 to f.location.y + 1) 
              if ((x != f.location.x || y != f.location.y) &&
                  (allowThroughCorners || x == f.location.x || y == f.location.y) &&
                  x >= 0 && x < width() &&
                  y >= 0 && y < width())} yield fields(x)(y);

                  
        val nextFrom = runFrom
        .view
        .flatMap(surroundings)
        .filter(condition)
        .distinct
        .force
        
        fillFields(fields, nextFrom, runFrom ++ filled, condition, allowThroughCorners)
      }
  }
  
}

