package cz.masci.javafx.demo.wizard;

import cz.masci.javafx.demo.wizard.controller.WizardController;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WizardDemoApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() {
    UserAgentBuilder.builder()
                    .themes(JavaFXThemes.MODENA)
                    .themes(MaterialFXStylesheets.forAssemble(false))
                    .setDeploy(true)
                    .setDebug(true)
                    .setResolveAssets(true)
                    .build()
                    .setGlobal();
  }

  @Override
  public void start(Stage primaryStage) {
    var wizardController = new WizardController();
    primaryStage.setScene(new Scene(wizardController.getView(), 600, 400));
    primaryStage.show();
  }
}
