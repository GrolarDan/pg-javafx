package cz.masci.javafx.demo.dto;

import cz.masci.javafx.demo.service.Modifiable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Daniel
 */
public class MonsterDTO implements Modifiable {
  private StringProperty name;
  private StringProperty description;

  public MonsterDTO(String name, String description) {
    nameProperty().set(name);
    descriptionProperty().set(description);
  }

  public final StringProperty nameProperty() {
    if (name == null) {
      name = new SimpleStringProperty();
    }
    return name;
  }
  
  public final StringProperty descriptionProperty() {
    if (description == null) {
      description = new SimpleStringProperty();
    }
    return description;    
  }
  
  public String getName() {
    return nameProperty().get();
  }

  public void setName(String name) {
    nameProperty().set(name);
  }

  public String getDescription() {
    return descriptionProperty().get();
  }

  public void setDescription(String description) {
    descriptionProperty().set(description);
  }
}
