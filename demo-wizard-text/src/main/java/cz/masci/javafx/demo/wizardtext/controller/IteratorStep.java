package cz.masci.javafx.demo.wizardtext.controller;

public interface IteratorStep extends LeafStep {
  void setParent(IteratorStep parent);
  LeafStep next();
  boolean hasNext();
  LeafStep prev();
  boolean hasPrev();

  String nextText();

  String prevText();

//  BooleanExpression nextDisabled();
//
//  BooleanExpression prevDisabled();

}
