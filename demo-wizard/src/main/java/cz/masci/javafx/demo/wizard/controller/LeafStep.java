package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class LeafStep implements WizardStep {
  private boolean active;
  private final Region view;

  @Setter
  private Consumer<StringProperty> updateTitle;
  @Setter
  private Consumer<StringProperty> updateNextText;
  @Setter
  private Consumer<StringProperty> updatePrevText;
  @Setter
  private Consumer<BooleanProperty> updateNextDisable;
  @Setter
  private Consumer<BooleanProperty> updatePrevDisable;

  @Override
  public void next(WizardViewModel wizardViewModel) {
    doStep(wizardViewModel);
  }

  @Override
  public void previous(WizardViewModel wizardViewModel) {
    doStep(wizardViewModel);
  }

  @Override
  public WizardStep current() {
    return active ? this : null;
  }

  @Override
  public Region getView() {
    return active ? view : null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public boolean hasPrevious() {
    return false;
  }

  private void doStep(WizardViewModel wizardViewModel) {
    active = !active;

    if (active) {
      updateWizardViewModel(wizardViewModel);
    } else {
      unbindWizardViewModel(wizardViewModel);
    }
  }

  private void updateWizardViewModel(WizardViewModel wizardViewModel) {
    consumeIfNotNull(updateTitle, wizardViewModel.titleProperty());
    consumeIfNotNull(updateNextText, wizardViewModel.nextTextProperty());
    consumeIfNotNull(updatePrevText, wizardViewModel.prevTextProperty());
    consumeIfNotNull(updateNextDisable, wizardViewModel.nextDisableProperty());
    consumeIfNotNull(updatePrevDisable, wizardViewModel.prevDisableProperty());
  }

  private void unbindWizardViewModel(WizardViewModel wizardViewModel) {
    unbindIfBound(wizardViewModel.titleProperty());
    unbindIfBound(wizardViewModel.nextTextProperty());
    unbindIfBound(wizardViewModel.prevTextProperty());
    unbindIfBound(wizardViewModel.nextDisableProperty());
    unbindIfBound(wizardViewModel.prevDisableProperty());
  }

  private <T> void consumeIfNotNull(Consumer<T> consumer, T value) {
    if (consumer != null) {
      consumer.accept(value);
    }
  }

  private <T extends Property<?>> void unbindIfBound(T property) {
    if (property.isBound()) {
      property.unbind();
    }
  }
}
