package cz.masci.javafx.demo.wizard.controller.second;

import java.util.Optional;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class SimpleLeafStep implements LeafStep {

  @Getter
  @Setter
  private HierarchicalStep parent;
  private final String title;
  private final Region view;

  @Override
  public String title() {
    return Optional.ofNullable(getParent())
                   .map(parent -> parent.title() == null || parent.title().isBlank() ? title : String.join(" - ", parent.title(), title))
                   .orElse("N/A");
  }

  @Override
  public Region view() {
    return view;
  }
}
