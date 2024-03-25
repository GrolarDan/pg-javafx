package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Step_3 implements LeafStep {

  private final BooleanProperty valid = new SimpleBooleanProperty(true);

  @Override
  public String title() {
    return "Main Step 3";
  }

  @Override
  public BooleanExpression isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }
}
