package cz.masci.javafx.demo.wizardtext.controller;

import java.util.function.Function;

public interface CompositeStep extends HierarchicalStep {
  default String title() {
    return "";
  }
  Step prev();
  Step next();
  boolean hasPrev();
  boolean hasNext();
  default <R> R applyOnIteratorStepOrNull(HierarchicalStep step, Function<CompositeStep, R> iteratorStepFunction) {
    return step instanceof CompositeStep compositeStep ? iteratorStepFunction.apply(compositeStep) : null;
  }
}
