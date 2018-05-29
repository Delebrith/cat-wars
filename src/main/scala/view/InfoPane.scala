package view

import logic.{Board, Player}
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._

/**
  * Pane containing a panel with message for user, image of player to make a move in current sitting,
  * and a score counter for both players
  * @param paneWidth width in pixels
  * @param paneHeight height in pixels
  * @param board structure with current state of the game
  * @param player Player to make a move
  */
class InfoPane(paneWidth: Double, paneHeight: Double, board: Board, player: Player) extends Pane {

  private val officerImage : VBox = new VBox {
    private val imageView = new ImageView {
      image = new Image(
        if (player.name == PlayerName.PLAYER.toString)
          "officer_cat.png"
        else
          "enemy_cat.png")
      fitHeight_=(paneHeight * 2)
      preserveRatio_=(true)
      alignmentInParent_=(Pos.BottomLeft)
    }
    styleClass_=(List("message-panel",
      if (player.name == PlayerName.PLAYER.toString)
        "message-panel-own"
      else
        "message-panel-enemy"))
    children_=(List(imageView))
  }

  private val scorePanel : VBox = new VBox() {
    hgrow_=(Priority.Always)
    vgrow_=(Priority.Always)
    alignmentInParent_=(Pos.BottomRight)

    private val playerName : Label = new Label(PlayerName.PLAYER.toString)
    private val playerScore : Label = new Label(board.numberOfPoints(Player(PlayerName.PLAYER.toString)).toString)

    private val computerName : Label = new Label(PlayerName.COMPUTER.toString)
    private val computerScore : Label = new Label(board.numberOfPoints(Player(PlayerName.COMPUTER.toString)).toString)

    alignment_=(Pos.Center)
    styleClass_=(List("score-panel",
      if (player.name == PlayerName.PLAYER.toString)
        "score-panel-own"
      else
        "score-panel-enemy"))
    children =  List(playerName, playerScore, computerName, computerScore)
  }

  private val messagePanel : VBox = new VBox() {
    minWidth_=(0.57 * paneWidth)
    alignment_=(Pos.Center)
    private val message =
      new Label("General!\n The enemies are coming! \nTo win the battle\n surround enemy soldiers\n with ours!")
    styleClass_=(List("message-panel",
      if (player.name == PlayerName.PLAYER.toString)
        "message-panel-own"
      else
        "message-panel-enemy"))
    children = List(message)
  }


  styleClass_=(List("info-pane", "label", "v-box", "image-view"))
  alignmentInParent_=(Pos.BottomCenter)
  maxHeight_=(paneHeight)
  children = new HBox{
    hgrow_=(Priority.Always)
    vgrow_=(Priority.Always)
    children = List(officerImage, messagePanel, scorePanel)
  }
}
