import logic.Board
import scalafx.application.JFXApp
import ui.{GameScene, StartScene}

object App extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 1024
    height = 768
    resizable = false
    scene = new GameScene(width.value, height.value, new Board(15, 9))
  }
}
