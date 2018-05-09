package logic

import scala.annotation.tailrec

case class Board(fields: Array[Array[Field]]) {
  /*
   * additional constructor that creates empty game board
   */
  def this(width: Int, height: Int) = this (
    (for (x <- (0 until width)) yield
      (for (y <- (0 until height)) yield
        Field(Point(x, y), None, None)
      ).toArray
    ).toArray
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
      for (row <- fields) yield
        for (field <- row) yield
          if (field.location == location)
            Field(location, Some(player), field.base)
            else field
    
    val boardBorders = 
      (for (x <- (0 until width())) yield List(fieldsWithPlacedDot(x)(0), fieldsWithPlacedDot(x).last))
      .++(for (y <- (1 until height() - 1)) yield List(fieldsWithPlacedDot(0)(y), fieldsWithPlacedDot(0)(y-1)))
      .flatten;
    val fieldsNotInBases = fillFields(
        fieldsWithPlacedDot,
        boardBorders,
        (f: Field) => f.dot == player && f.base.getOrElse(player) == player, false)
    
    val fieldsInBases = 
      (for (
          field <- fieldsWithPlacedDot.flatten
            if field.dot.getOrElse(player) != player && field.base.getOrElse(player) != player)
        yield fillFields(
            fieldsWithPlacedDot,
            List(field),
            (f: Field) => fieldsNotInBases.contains(f),
            true))
      .flatten
    
    val newFields = 
      (for (row <- fieldsWithPlacedDot) yield 
        (for (field <- row) yield
          if (fieldsInBases.contains(field)) Field(field.location, field.dot, Some(player)) else field)
        .toArray)
      .toArray 
    
    return Board(newFields)
  }
  
  private def fillFields(
      fields: Array[Array[Field]],
      startFrom: Seq[Field],
      condition: Field => Boolean,
      allowThroughCorners: Boolean): Seq[Field] = {
    val filteredStartFrom = startFrom.filter(condition)
    fillFields(fields, startFrom, Seq[Field](), condition, allowThroughCorners)
  }
  
  @tailrec
  private def fillFields(
      fields: Array[Array[Field]],
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

                  
        val nextFrom = runFrom.map(surroundings).flatten.filter(condition).distinct
        
        fillFields(fields, nextFrom, runFrom ++ filled, condition, allowThroughCorners)
      }
  }
  
}

