package cz.masci.javafx.demo.wizardtext.controller;

import java.util.Optional;

public interface LeafStep extends HierarchicalStep {

  String getTitle();

  default String title() {
    return Optional.ofNullable(getParent())
                   .map(parent -> String.join(parent.title(), getTitle()))
                   .orElse("N/A");
  }

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