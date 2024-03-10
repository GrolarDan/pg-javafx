package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import javafx.scene.layout.Region;

public interface WizardStep {

  void next(WizardViewModel wizardViewModel);
  void previous(WizardViewModel wizardViewModel);
  WizardStep current();
  Region getView();
  boolean hasNext();
  boolean hasPrevious();
}
