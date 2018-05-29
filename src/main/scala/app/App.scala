package app

import scalafx.application.JFXApp
import view.StartScene

/**
  * Instance of the game window
  */
object App extends JFXApp {
  def instance : JFXApp = this
  stage = new JFXApp.PrimaryStage {
    title.value = "Cat wars!"
    width = 1024
    height = 768
    resizable = false
    scene = new StartScene(width.value, height.value)
  }
}
