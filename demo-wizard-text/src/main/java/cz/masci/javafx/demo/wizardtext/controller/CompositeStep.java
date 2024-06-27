package cz.masci.javafx.demo.wizardtext.controller;

import java.util.function.Function;

public interface CompositeStep extends HierarchicalStep {
  Step prev();

  Step next();

  boolean hasPrev();

  boolean hasNext();

  @Override
  default String title() {
    return "";
  }

  default <R> R applyOnCompositeStepOrNull(HierarchicalStep step, Function<CompositeStep, R> iteratorStepFunction) {
    return step instanceof CompositeStep compositeStep ? iteratorStepFunction.apply(compositeStep) : null;
  }
}
