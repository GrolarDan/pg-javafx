package cz.masci.javafx.demo.wizard.controller.first;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import javafx.scene.layout.Region;

public interface WizardStep {

  void next(WizardViewModel wizardViewModel);
  void previous(WizardViewModel wizardViewModel);
  void reset();
  WizardStep current();
  Region getView();
  boolean hasNoNext();
  boolean hasNoPrevious();
}
