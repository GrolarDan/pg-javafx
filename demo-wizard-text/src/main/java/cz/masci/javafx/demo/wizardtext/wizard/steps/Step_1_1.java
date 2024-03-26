package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

public class Step_1_1 implements LeafStep {

  private final BooleanProperty valid = new SimpleBooleanProperty(true);
  @Getter
  @Setter
  private LeafStep parent;

  @Override
  public String title() {
    return "Main Step 1-1";
  }

  @Override
  public BooleanExpression isValid() {
    return valid;
  }
}
