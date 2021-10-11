package cz.masci.javafx.demo;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class DetailController {

  private ChangeListener<String> listener;
  private Monster monster;

  @FXML
  private Label name;

  @FXML
  private TextArea description;

  public void setMonster(Monster monster) {
    if (this.monster != null) {
      unhookListener();
    }
    this.monster = monster;
    hookTo(monster);
  }

  private void unhookListener() {
    description.textProperty().removeListener(listener);
  }

  private void hookTo(Monster monster) {
    if (monster == null) {
      name.setText("");
      description.setText("");
      listener = null;
    } else {
      name.setText(monster.getName());
      description.setText(monster.getDescription());
      listener = (observable, oldValue, newValue)
              -> monster.setDescription(newValue);
      description.textProperty().addListener(listener);
    }
  }

}
