package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.HierarchicalStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import lombok.Data;

@Data
public class Step_1_3 implements LeafStep {

  private HierarchicalStep parent;
  private String title = "Main Step 1-3";
}
