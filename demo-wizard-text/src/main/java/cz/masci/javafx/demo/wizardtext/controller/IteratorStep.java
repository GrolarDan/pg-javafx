package cz.masci.javafx.demo.wizardtext.controller;

public interface IteratorStep extends LeafStep<IteratorStep> {
//  void setParent(IteratorStep parent);
  LeafStep<IteratorStep> next();
  boolean hasNext();
  LeafStep<IteratorStep> prev();
  boolean hasPrev();

//  BooleanExpression nextDisabled();
//
//  BooleanExpression prevDisabled();

}
