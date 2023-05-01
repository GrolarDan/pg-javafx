package cz.masci.javafx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("slide.fxml"));
    var scene = new Scene(fxmlLoader.load(), 640, 480);
    stage.setScene(scene);
    stage.show();
  }

}
