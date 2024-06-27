package cz.masci.javafx.demo.wizardtext.wizard.steps;

public class Step_1_Composite extends BaseCompositeStep {

  @Override
  protected String getPrevText() {
    return "Previous Step 1";
  }

  @Override
  protected String getNextText() {
    return "Next Step 1";
  }
}
