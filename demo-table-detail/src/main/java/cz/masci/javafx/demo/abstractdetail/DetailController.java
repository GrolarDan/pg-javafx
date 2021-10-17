package cz.masci.javafx.demo.abstractdetail;

import cz.masci.javafx.demo.dto.MonsterDTO;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DetailController extends AbstractDetailController<MonsterDTO> {

  @FXML
  private Label name;

  @FXML
  private TextArea description;

  @Override
  protected List<ObservableValue<String>> initObservableValues() {
    log("initObservableValues");
    
    return List.of(
            description.textProperty()
    );
  }

  @Override
  protected void fillInputs(MonsterDTO monster) {
    log("fillInputs: " + monster);
    
    if (monster == null) {
      name.setText("");
      description.setText("");

    } else {
      name.setText(monster.getName());
      description.setText(monster.getDescription());
    }
  }

  @Override
  protected void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    log("changes - newValue: " + newValue);
    log("changes - observable: " + observable);
    
    if (description.textProperty().equals(observable)) {
      getItem().setDescription(newValue);
    }
  }

  private void log(String text) {
    log.info("{} : {}", this.getClass().getCanonicalName(), text);    
  }

}
