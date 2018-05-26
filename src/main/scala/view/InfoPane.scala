package view

import logic.{Board, Player}
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._

class InfoPane(paneWidth: Double, paneHeight: Double, board: Board) extends Pane {

  val officerImage : VBox = new VBox {
    val imageView = new ImageView {
      image = new Image("officer_cat.png")
      fitHeight_=(paneHeight * 2)
      preserveRatio_=(true)
      alignmentInParent_=(Pos.BottomLeft)
    }
    styleClass_=(List("message-panel"))
    children_=(List(imageView))
  }

  val scorePanel : VBox = new VBox() {
    hgrow_=(Priority.Always)
    vgrow_=(Priority.Always)
    alignmentInParent_=(Pos.BottomRight)

    val playerName : Label = new Label(PlayerName.PLAYER.toString)
    val playerScore : Label = new Label(board.numberOfPoints(Player(PlayerName.PLAYER.toString)).toString)

    val computerName : Label = new Label(PlayerName.COMPUTER.toString)
    val computerScore : Label = new Label(board.numberOfPoints(Player(PlayerName.COMPUTER.toString)).toString)

    alignment_=(Pos.Center)
    styleClass_=(List("score-panel"))
    children =  List(playerName, playerScore, computerName, computerScore)
  }

  val messagePanel : VBox = new VBox() {
    minWidth_=(0.57 * paneWidth)
    alignment_=(Pos.Center)
    val message =
      new Label("General!\n The enemies are coming! \nTo win the battle\n surround enemy soldiers\n with ours!")
    styleClass_=(List("message-panel"))
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
