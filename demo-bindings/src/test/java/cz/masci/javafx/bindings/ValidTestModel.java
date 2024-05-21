package cz.masci.javafx.bindings;

import cz.masci.springfx.mvci.model.detail.ValidModel;
import io.github.palexdev.materialfx.validation.MFXValidator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ValidTestModel implements ValidModel {
  @Getter
  private final MFXValidator validator = new MFXValidator();
  private final StringProperty name = new SimpleStringProperty();
  private final IntegerProperty age = new SimpleIntegerProperty();

  public ValidTestModel() {
    validator.constraint("Name is required", name.isNotEmpty());
    validator.constraint("Age must be 18+", age.greaterThan(18));
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public int getAge() {
    return age.get();
  }

  public IntegerProperty ageProperty() {
    return age;
  }

  public void setAge(int age) {
    this.age.set(age);
  }
}
