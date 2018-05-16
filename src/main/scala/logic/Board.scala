package logic

import scala.annotation.tailrec

case class Board(fields: Vector[Vector[Field]]) {
  /*
   * additional constructor that creates empty game board
   */
  def this(width: Int, height: Int) = this (
    (for (y <- (0 until height)) yield
      (for (x <- (0 until width)) yield
        Field(Point(x, y), None, None)
      ).toVector
    ).toVector
  )
  
  def width(): Int = {
    if (fields.length > 0) fields(0).length else 0;
  }
  def height(): Int = {
    fields.length;
  }
  
  def placeDot(
      location: Point,
      player: Player): Board = {
    val newFields =
      fields.map(_.map((f: Field) =>
        if (f.location == location) 
          Field(location, Some(player), f.base) 
        else 
          f))

    val boardBorders = 
      (for (x <- (0 until width())) yield List(newFields(0)(x), newFields.last(x)))
      .++(for (y <- (1 until height() - 1)) yield List(newFields(y)(0), newFields(y).last))
      .flatten
    
    val fieldsNotInBases = fillFields(
      newFields,
      boardBorders,
      (f: Field) => !(f.dot == Some(player) && f.base == None || f.base == Some(player)), false)
    
    //get enemy dots that are not in our bases
    val fieldsInBases = newFields
      .flatten
      .filter(field => field.dot.getOrElse(player) != player && field.base != Some(player))
      .flatMap(field => fillFields(
          newFields,
          List(field),
          (f: Field) => !fieldsNotInBases.contains(f),
          true))
    
    Board(
      newFields
        .map(_.map(field => 
          if (fieldsInBases.contains(field))
            Field(field.location, field.dot, Some(player)) 
          else
            field))
      )
  }
  
  private def fillFields(
      fields: Vector[Vector[Field]],
      startFrom: Seq[Field],
      condition: Field => Boolean,
      allowThroughCorners: Boolean): Seq[Field] = {
    val filteredStartFrom = startFrom.filter(condition)
    fillFields(fields, filteredStartFrom, Seq[Field](), condition, allowThroughCorners)
  }
  
  @tailrec
  private def fillFields(
      fields: Vector[Vector[Field]],
      runFrom: Seq[Field],
      filled: Seq[Field],
      condition: Field => Boolean,
      allowThroughCorners: Boolean): Seq[Field] = {
      if (runFrom.length == 0) 
        filled
      else {
        
        val surroundings = (f: Field) => for {
            x <- (f.location.x - 1 to f.location.x + 1)
            y <- (f.location.y - 1 to f.location.y + 1) 
              if ((x != f.location.x || y != f.location.y) &&
                  (allowThroughCorners || x == f.location.x || y == f.location.y) &&
                  x >= 0 && x < width() &&
                  y >= 0 && y < width())} yield fields(y)(x);

                  
        val nextFrom = runFrom
        .view
        .flatMap(surroundings)
        .filter(f => !filled.contains(f))
        .filter(f => !runFrom.contains(f))
        .filter(condition)
        .distinct
        .force
        
        fillFields(fields, nextFrom, runFrom ++ filled, condition, allowThroughCorners)
      }
  }
  
}

