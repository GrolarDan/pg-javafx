package cz.masci.javafx.homescreen.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HelloController implements Initializable {

  private final String welcomeText;

  @FXML
  private Label welcomeTextLabel;

  public HelloController(String welcomeText) {
    this.welcomeText = welcomeText;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    welcomeTextLabel.setText(welcomeText);
  }
}