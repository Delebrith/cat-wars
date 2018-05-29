package logic

/**
  * Class for invalid moves of a player
  * 
  * @constructor Creates a throw-ready exception
  * 
  * @param message Summary of reason behind throw
  * @param cause Exception that caused this to be thrown
  */
case class GameLogicException(message: String = "", cause: Throwable = None.orNull)
  extends Exception(message, cause)