package cz.masci.javafx.demo.wizardtext.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public interface HierarchicalStep extends Step {
  BooleanProperty TRUE_PROPERTY = new ReadOnlyBooleanWrapper(true);
  void setParent(HierarchicalStep parent);
  HierarchicalStep getParent();
}
