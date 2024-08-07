package cz.masci.javafx.demo.wizard.controller.second;

import java.util.Optional;

public interface LeafStep extends HierarchicalStep {

  default String prevText() {
    return Optional.ofNullable(getParent())
                   .map(HierarchicalStep::prevText)
                   .orElse(null);
  }

  default String nextText() {
    return Optional.ofNullable(getParent())
                   .map(HierarchicalStep::nextText)
                   .orElse(null);
  }
}