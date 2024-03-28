package cz.masci.javafx.demo.wizard.controller.second;

import javafx.scene.layout.Region;

public interface IteratorStep extends LeafStep {
  void setParent(IteratorStep parent);
  LeafStep next();
  boolean hasNext();
  LeafStep prev();
  boolean hasPrev();

  String nextText();

  String prevText();

  default Region view() {
    return null;
  }
//  BooleanExpression nextDisabled();
//
//  BooleanExpression prevDisabled();

}
