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
  * Class for window that is supposed to contain a simple and short message for player about a single exception and OK button that closes it
  * 
  * @constructor creates window with the message of given exception
  * 
  * @param exception The exception that's message will be displayed
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