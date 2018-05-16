import org.scalatest._

import logic._

class LogicTest extends FlatSpec {
  "Newly created board" should "contain no dots or bases" in {
    val board = new Board(4, 3)
    
    assert(board.width() == 4)
    assert(board.height() == 3)
    
    assert(board.fields.flatten.length == 4 * 3)
    
    for (x <- (0 until 4))
      for (y <- (0 until 3))
        assert(board.fields(y)(x) == Field(Point(x, y), None, None))
  }
  
  "Board after adding a dot" should "contain the dot only in selected field" in {
    val board = new Board(5, 5)
    val player = Player("test")
    val location = Point(2, 4)
    
    val newBoard = board.placeDot(location, player)
    
    for (x <- (0 until 5))
      for (y <- (0 until 5)) {
        if (x == location.x && y == location.y)
          assert(newBoard.fields(y)(x) == Field(Point(x, y), Some(player), None))
        else
          assert(newBoard.fields(y)(x) == Field(Point(x, y), None, None))
      }
  }
  
  "Board after surrounding dots" should "have created bases" in {
    /*
     * Board before:
     * 1 1 2 2
     * 1 2 1 2
     * . 2 1 2
     * . 1 . .
     * with no bases.
     * 
     * Board after:
     * 1 1 2 2
     * 1 2 1 2
     * 1 2 1 2
     * . 1 . .
     * with bases:
     * 1 1 . .
     * 1 1 1 .
     * 1 1 1 .
     * . 1 . .
     */
    
    val player1 = Player("1")
    val player2 = Player("2")

    val player1dots = Seq(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 3), Point(2, 1), Point(2, 2))
    val player2dots = Seq(Point(1, 1), Point(1, 2), Point(2, 0), Point(3, 0), Point(3, 1), Point(3, 2))
    
    val board = Board(
      (for (y <- (0 until 4)) yield (for (x <- (0 until 4)) yield 
          if (player1dots.contains(Point(x, y)))
            Field(Point(x, y), Some(player1), None)
          else if (player2dots.contains(Point(x, y)))
            Field(Point(x, y), Some(player2), None)
          else
            Field(Point(x, y), None, None)
        ).toVector
      ).toVector
    )
    
    
    val newBoard = board.placeDot(Point(0, 2), player1)
    
    val expectedPlayer1dots = Point(0, 2) +: player1dots
    
    val expectedBases = 
      (for (y <- (0 until 3)) yield Point(0, y))
      .++(for (y <- (0 until 4)) yield Point(1, y))
      .++(for (y <- (1 until 3)) yield Point(2, y))
    
    for (field <- newBoard.fields.flatten) {
      if (expectedPlayer1dots.contains(field.location))
        assert(field.dot == Some(player1))
      else if (player2dots.contains(field.location))
        assert(field.dot == Some(player2))
      else
        assert(field.dot == None)
      
      if (expectedBases.contains(field.location))
        assert(field.base == Some(player1))
      else
        assert(field.base == None)
    }
  }
  
  "Placing dot on an already existing one" should "throw an exception" in {
    val player = Player("")
    val board = Board(Vector(Vector(Field(Point(0, 0), Some(player), None))))
    
    assertThrows[GameLogicException] {
      board.placeDot(Point(0, 0), player)
    }
  }
  
  
  
  "Placing dot in a base" should "throw an exception" in {
    val player = Player("")
    
    /*
     * Board:
     * 1 1 1
     * 1 . 1
     * 1 1 1
     * With bases:
     * 1 1 1
     * 1 1 1
     * 1 1 1
     */
    
    val board = Board(Vector(
        Vector(Field(Point(0, 0), Some(player), Some(player)), Field(Point(0, 1), Some(player), Some(player)), Field(Point(0, 2), Some(player), Some(player))),
        Vector(Field(Point(1, 0), Some(player), Some(player)), Field(Point(1, 1), None, Some(player)), Field(Point(1, 2), Some(player), Some(player))),
        Vector(Field(Point(2, 0), Some(player), Some(player)), Field(Point(2, 1), Some(player), Some(player)), Field(Point(2, 2), Some(player), Some(player)))
      ))

    assertThrows[GameLogicException] {
      board.placeDot(Point(1, 1), player)
    }
  }
  
  "Placing dot outside of the board " should "throw an exception" in {
    val board = new Board(3, 4)
    val player = Player("")
    
    assertThrows[GameLogicException] {
      board.placeDot(Point(-1, 1), player)
    }
    assertThrows[GameLogicException] {
      board.placeDot(Point(1, -1), player)
    }
    assertThrows[GameLogicException] {
      board.placeDot(Point(6, 1), player)
    }
    assertThrows[GameLogicException] {
      board.placeDot(Point(1, 5), player)
    }
  }
  
  "List of connections of empty board" should "be empty" in {
    val board = new Board(2, 5)
    
    assert(board.connections.isEmpty)
  }
  
  "List of connections of board with base" should "be the border of the base" in {
    /*
     * Board:
     * 1 1 1 2
     * 1 2 1 2
     * 2 1 1 .
     * 2 1 2 2
     * with bases:
     * 1 1 . .
     * 1 1 1 .
     * . 1 1 .
     * .  . .
     * 
     */
    
    val p1 = Player("1")
    val p2 = Player("2")
    
    val board = Board(Vector(
        Vector(Field(Point(0, 0), Some(p1), Some(p1)), Field(Point(0, 1), Some(p1), Some(p1)), Field(Point(0, 2), Some(p1), None), Field(Point(0, 3), Some(p2), None)),
        Vector(Field(Point(1, 0), Some(p1), Some(p1)), Field(Point(1, 1), Some(p2), Some(p1)), Field(Point(1, 2), Some(p1), Some(p1)), Field(Point(1, 3), Some(p2), None)),
        Vector(Field(Point(2, 0), Some(p2), None), Field(Point(2, 1), Some(p1), Some(p1)), Field(Point(2, 2), Some(p1), Some(p1)), Field(Point(2, 3), None, None)),
        Vector(Field(Point(3, 0), Some(p2), None), Field(Point(3, 1), Some(p1), None), Field(Point(3, 2), Some(p2), None), Field(Point(3, 3), Some(p2), None))
      ))
          
    val expectedConnections = Seq((Point(0, 0), Point(0, 1)), (Point(0, 1), Point(1, 2)), (Point(1, 2), Point(2, 2)),
        (Point(2, 2), Point(2, 1)), (Point(2, 1), Point(1, 0)), (Point(1, 0), Point(0, 0)))
    
    val connections = board.connections()
 
    for (conn <- connections)
      assert(expectedConnections.contains(conn) || expectedConnections.contains(conn.swap))
    for (conn <- expectedConnections)
      assert(connections.contains(conn) || connections.contains(conn.swap))
  }
}