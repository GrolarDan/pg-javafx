package cz.masci.javafx.demo.wizardtext.controller;

import javafx.beans.binding.BooleanExpression;

public interface Step {
  String title();
  BooleanExpression valid();
  String prevText();
  String nextText();
}
