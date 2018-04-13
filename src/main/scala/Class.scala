import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color._

object HelloStageDemo extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600
    height = 450
    scene = new Scene {
      fill = LightGreen
      content = new ImageView {image = new Image(this, "armycat.jpg")}
    }
  }
}
