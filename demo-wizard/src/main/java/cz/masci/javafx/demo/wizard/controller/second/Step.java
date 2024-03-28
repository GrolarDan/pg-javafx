package cz.masci.javafx.demo.wizard.controller.second;

import javafx.beans.binding.BooleanExpression;
import javafx.scene.layout.Region;

public interface Step {
  // All these methods are calculated from bottom to top
  Region view();

  String title();

  BooleanExpression valid();

  String prevText();

  String nextText();
}
