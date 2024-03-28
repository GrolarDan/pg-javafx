package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.HierarchicalStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import javafx.beans.binding.BooleanExpression;
import lombok.Data;

@Data
public class Step_1_1 implements LeafStep {

  private HierarchicalStep parent;

  @Override
  public String title() {
    return "Main Step 1-1";
  }

  @Override
  public BooleanExpression valid() {
    return TRUE_PROPERTY;
  }
}
