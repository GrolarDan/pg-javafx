package cz.masci.javafx.demo.wizard.controller.second;

import java.util.Optional;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public interface HierarchicalStep extends Step {
  BooleanProperty TRUE_PROPERTY = new ReadOnlyBooleanWrapper(true);

  void setParent(HierarchicalStep parent);

  HierarchicalStep getParent();

  @Override
  default BooleanExpression valid() {
    return Optional.ofNullable(getParent())
                   .map(Step::valid)
                   .orElse(TRUE_PROPERTY);
  }

  @Override
  default String title() {
    return Optional.ofNullable(getParent())
                   .map(Step::title)
                   .orElse(null);
  }
}
