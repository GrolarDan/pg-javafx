package cz.masci.javafx.demo.wizardtext.controller;

import javafx.beans.binding.BooleanExpression;

public interface LeafStep<T extends LeafStep<T>> {
//  String view();

  String title();

  BooleanExpression isValid();

  T getParent();

  void setParent(T parent);

  default String nextText() {
    return getParent() != null ? getParent().nextText() : null;
  }

  default String prevText() {
    return getParent() != null ? getParent().prevText() : null;
  }

}