package cz.masci.javafx.demo.wizardtext.controller;

import javafx.beans.binding.BooleanExpression;

public interface LeafStep {
//  String view();

  String title();

  BooleanExpression isValid();
}