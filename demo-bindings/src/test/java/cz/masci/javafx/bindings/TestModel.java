package cz.masci.javafx.bindings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// region model
public class TestModel {
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getDescription() {
    return description.get();
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }
}
