package app

import scalafx.application.JFXApp
import view.StartStage

object App extends JFXApp {
  def instance : JFXApp = this
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 1024
    height = 768
    resizable = false
    scene = new StartStage(width.value, height.value)
  }
}
