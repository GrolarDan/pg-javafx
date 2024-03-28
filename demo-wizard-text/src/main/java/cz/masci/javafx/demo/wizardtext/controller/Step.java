package cz.masci.javafx.demo.wizardtext.controller;

import javafx.beans.binding.BooleanExpression;

public interface Step {
  // All these methods are calculated from bottom to top
  String title();

  BooleanExpression valid();

  String prevText();

  String nextText();
}
