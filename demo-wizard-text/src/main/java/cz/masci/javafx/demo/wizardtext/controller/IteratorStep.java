package cz.masci.javafx.demo.wizardtext.controller;

public interface IteratorStep extends LeafStep {
  void setParent(IteratorStep parent);
  LeafStep next();
  boolean hasNext();
  LeafStep prev();
  boolean hasPrev();

//  BooleanExpression nextDisabled();
//
//  BooleanExpression prevDisabled();

//  String nextText();

//  String prevText();
}
