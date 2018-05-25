package app

import scalafx.application.JFXApp
import view.StartScene

object App extends JFXApp {
  def instance : JFXApp = this
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 1024
    height = 768
    resizable = false
    scene = new StartScene(width.value, height.value)
  }
}
