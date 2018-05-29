package view

import scalafx.stage.Stage
import logic.GameLogicException
import app.App
import javafx.stage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.stage.Modality
import scalafx.stage.StageStyle

/**
  * Dialog displayed after player's attempt to violate rules of the game
  *
  * @param exception Exception carrying message with explanation of which rule was violated
  */
class ExceptionStage(exception : GameLogicException) extends Stage {
  scene_=(new Scene {
    
    stylesheets_=(List(getClass.getClassLoader.getResource("app/style.css").toExternalForm))
    
    content_=(new VBox{
      styleClass_=(List("dialog"))
      alignmentInParent_=(Pos.Center)
      alignment_=(Pos.BottomCenter)
      hgrow_=(Priority.Always)
      vgrow_=(Priority.Always)
      children = List(
          new Label(exception.message) {
            styleClass_=(List("dialog-label"))
          }, 
          new Button("OK") {
            handleEvent(MouseEvent.MouseReleased) {
              e: MouseEvent => {
                window.value.asInstanceOf[stage.Stage].close()
              }
            }
            styleClass_=(List("menu-button"))
          })
    })

  })
  
  
  initOwner(App.stage)
  initModality(Modality.WindowModal)
  initStyle(StageStyle.Undecorated)  
}