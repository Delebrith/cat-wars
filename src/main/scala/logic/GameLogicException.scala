package logic

case class GameLogicException(message: String = "", cause: Throwable = None.orNull)
  extends Exception(message, cause)