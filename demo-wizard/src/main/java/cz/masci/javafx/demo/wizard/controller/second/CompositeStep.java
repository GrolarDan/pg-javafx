package cz.masci.javafx.demo.wizard.controller.second;

import java.util.function.Function;
import javafx.scene.layout.Region;

public interface CompositeStep extends HierarchicalStep {
  Step prev();

  Step next();

  boolean hasPrev();

  boolean hasNext();

  @Override
  default String title() {
    return "";
  }

  // Composite step should not have a view
  @Override
  default Region view() {
    return null;
  }

  default <R> R applyOnCompositeStepOrNull(HierarchicalStep step, Function<CompositeStep, R> iteratorStepFunction) {
    return step instanceof CompositeStep compositeStep ? iteratorStepFunction.apply(compositeStep) : null;
  }
}
