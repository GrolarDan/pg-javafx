package cz.masci.javafx.homescreen;

import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

//  private int count = 0;

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(FXResourcesLoader.loadURL("fxml/home-screen.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    MFXThemeManager.addOn(scene, Themes.DEFAULT);
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();

    stage.setOnCloseRequest(event -> {
//      if (count++ < 3) {
//        event.consume();
//      }
    });
  }

  public static void main(String[] args) {
    launch();
  }
}