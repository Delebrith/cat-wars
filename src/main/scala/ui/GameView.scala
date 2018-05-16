package ui

import javafx.scene.text.TextAlignment
import logic.Board
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.BackgroundImage
import scalafx.scene.paint.Paint
import scalafx.scene.text.Font

object GameView {

  def generateBoardView(board: Board, width: Double, height: Double) : List[Button] = {
    val columns = board.width()
    val rows = board.height()
    val fields = Array.ofDim[Button](rows, columns)
    for (i <- 0 until fields.length; j <- 0 until fields(i).length){
        fields(i)(j) = new Button ()
        fields(i)(j).setPrefSize((height - 350)/rows, (height - 350)/rows)
        fields(i)(j).setLayoutX(20 + (fields(i)(j).getPrefWidth() + 5) * j)
        fields(i)(j).setLayoutY(20 + (fields(i)(j).getPrefHeight() + 5) * i)
        fields(i)(j).setPadding(Insets.apply(0))
        val image = new ImageView(new Image("player_soldier_0" + ((i*j)%4 + 1) + ".png"))
        image.fitWidth_=(fields(i)(j).getPrefWidth)
        image.fitHeight_=(fields(i)(j).getPrefHeight)
        fields(i)(j).setGraphic(image)
    }
    fields.flatten.toList
  }

  def generateScoreLabels(playersName: String, playersScore: Integer, computerScore: Integer): List[Label] = {
    val playerNameLabel = new Label((playersName + ": ").toUpperCase)
    val computerNameLabel = new Label(("Computer: ").toUpperCase)
    val playerScoreLabel = new Label(playersScore.toString)
    val computerScoreLabel = new Label(computerScore.toString)

    playerNameLabel.setLayoutY(120)
    playerScoreLabel.setLayoutY(180)
    computerNameLabel.setLayoutY(240)
    computerScoreLabel.setLayoutY(300)

    val list = List(playerNameLabel, playerScoreLabel, computerNameLabel, computerScoreLabel)
    for (label <- list){
      label.setTextAlignment(TextAlignment.CENTER)
      label.setLayoutX(840)
      label.setTextFill(Paint.valueOf("DarkOrange"))
      label.setPrefSize(180, 80)
      label.setFont(Font.apply("Arial Black", 24))
    }
    list
  }

  def generfateOfficerImage(width: Double, height: Double): ImageView ={
    val image = new Image("officer_cat.png")
    val imageView = new ImageView(image);
    imageView.setPreserveRatio(true)
    imageView.fitHeight_=(230)
    imageView.setLayoutX(0)
    imageView.setLayoutY(height - imageView.getFitHeight - 30)
    imageView
  }
}
