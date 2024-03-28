package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.HierarchicalStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Data;

@Data
public class Step_1_2 implements LeafStep {

  private final BooleanProperty valid = new SimpleBooleanProperty(true);
  private HierarchicalStep parent;

  @Override
  public String title() {
    return "Main Step 1-2";
  }

  @Override
  public BooleanExpression valid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }
}
