package cz.masci.javafx.demo.button;

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

  Random random = new Random();
  
  @Override
  public void start(Stage stage) {
    var javaVersion = SystemInfo.javaVersion();
    var javafxVersion = SystemInfo.javafxVersion();

    var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    var hello = new Label();
    var button = new Button("Hello");
    button.setOnAction((event) -> {
      hello.setText(" HELLO " + random.nextInt());
    });
    var vbox = new VBox(label, button, hello);
    var scene = new Scene(vbox, 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

}
