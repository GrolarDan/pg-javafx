package cz.masci.javafx.demo.wizard.controller.second;

import javafx.beans.binding.BooleanExpression;
import javafx.scene.layout.Region;

public interface LeafStep {
  Region view();

  String title();

  BooleanExpression isValid();
}