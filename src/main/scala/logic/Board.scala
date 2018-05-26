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
    
    if (location.x < 0 || location.y < 0 || location.x >= width() || location.y >= height())
      throw GameLogicException("Location is out of board")
    
    val newFields =
      fields.map(_.map((f: Field) =>
        if (f.location == location) 
          f.placeDot(player) 
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
  
  def connections(): Seq[(Point, Point)] = {
    
    def isConnectedToBottomLeft(field: Field): Boolean = {
      val to = fields(field.location.x - 1)(field.location.y + 1)
      val left = fields(field.location.x - 1)(field.location.y)
      val bottom = fields(field.location.x)(field.location.y + 1)
      
      to.dot == field.dot && to.base == field.base && 
        (left.base == field.base && bottom.base != field.base ||
         left.base != field.base && bottom.base == field.base)
    }
    def isConnectedToBottomRight(field: Field): Boolean = {
      val to = fields(field.location.x + 1)(field.location.y + 1)
      val right = fields(field.location.x + 1)(field.location.y)
      val bottom = fields(field.location.x)(field.location.y + 1)
      
      to.dot == field.dot && to.base == field.base && 
        (right.base == field.base && bottom.base != field.base ||
         right.base != field.base && bottom.base == field.base)
    }
    
    def isConnectedToBottom(field: Field): Boolean = {
      val to = fields(field.location.x)(field.location.y + 1)
      
      val left = field.location.x - 1
      val right = field.location.x + 1
      val y = field.location.y
      val bottom  = field.location.y + 1
      
      if (to.dot == field.dot && to.base == field.base) {
        if (field.location.x == 0 || field.location.x == width() - 1)
          true
        else
          fields(left)(y).base != field.base &&
          fields(left)(bottom).base != field.base &&
          fields(right)(y).base == field.base &&
          fields(right)(bottom).base == field.base ||
          fields(left)(y).base == field.base &&
          fields(left)(bottom).base == field.base &&
          fields(right)(y).base != field.base &&
          fields(right)(bottom).base != field.base
      }
      else
        false
    }
    
    def isConnectedToRight(field: Field): Boolean = {
      val to = fields(field.location.x + 1)(field.location.y)
      
      val top = field.location.y - 1
      val bottom = field.location.y + 1
      val x = field.location.x
      val right = field.location.x + 1
      if (to.dot == field.dot && to.base == field.base) {
        if (field.location.x == 0 || field.location.x == width() - 1)
          true
        else
          fields(x)(top).base != field.base &&
          fields(right)(top).base != field.base &&
          fields(x)(bottom).base == field.base &&
          fields(right)(bottom).base == field.base ||
          fields(x)(top).base == field.base &&
          fields(right)(top).base == field.base &&
          fields(x)(bottom).base != field.base &&
          fields(right)(bottom).base != field.base
      }
      else
        false
    }
    
    val ret = for (field <- fields.flatten if field.base != None) yield {
      val x = field.location.x
      val y = field.location.y
      
      val bottomLeft = 
        if (x - 1 >= 0 && y + 1 < height() && isConnectedToBottomLeft(field))
          Some(fields(x - 1)(y + 1).location)
        else
          None
      val bottom = 
        if (y + 1 < height() && isConnectedToBottom(field))
          Some(fields(x)(y + 1).location)
        else
          None
      val bottomRight =
        if (x + 1 < width() && y + 1 < height() && isConnectedToBottomRight(field))
          Some(fields(x + 1)(y + 1).location)
        else
          None
      val right =  
        if (x + 1 < width() && isConnectedToRight(field))
          Some(fields(x + 1)(y).location)
        else
          None
          
      Seq(bottomLeft, bottomRight, bottom, right)
    }.flatten.map((field.location, _))
    
    ret.flatten
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

