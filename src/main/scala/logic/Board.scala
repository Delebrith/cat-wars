package logic

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
}

