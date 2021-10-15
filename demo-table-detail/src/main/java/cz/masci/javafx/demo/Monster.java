package cz.masci.javafx.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Daniel
 */
public class Monster {
  private StringProperty name;
  private StringProperty description;
  private BooleanProperty edited; // TODO: change to modified

  public Monster(String name, String description) {
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
  
  public final BooleanProperty editedProperty() {
    if (edited == null) {
      edited = new SimpleBooleanProperty(false);
    }
    return edited;
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
  
  public Boolean isEdited() {
    return editedProperty().get();
  }
  
  public void setEdited(boolean edited) {
    editedProperty().set(edited);
  }
}
