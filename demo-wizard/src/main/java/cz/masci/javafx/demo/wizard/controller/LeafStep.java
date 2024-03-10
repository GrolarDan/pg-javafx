package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import lombok.Builder;

public class LeafStep implements WizardStep {
  private boolean active = false;

  private final Region view;
  private final Consumer<StringProperty> updateTitle;
  private final Consumer<StringProperty> updateNextText;
  private final Consumer<StringProperty> updatePrevText;
  private final Consumer<BooleanProperty> updateNextDisable;
  private final Consumer<BooleanProperty> updatePrevDisable;

  @Builder
  public LeafStep(Region view, Consumer<StringProperty> updateTitle, Consumer<StringProperty> updateNextText, Consumer<StringProperty> updatePrevText,
                  Consumer<BooleanProperty> updateNextDisable, Consumer<BooleanProperty> updatePrevDisable) {
    this.view = view;
    this.updateTitle = updateTitle;
    this.updateNextText = updateNextText;
    this.updatePrevText = updatePrevText;
    this.updateNextDisable = updateNextDisable;
    this.updatePrevDisable = updatePrevDisable;
  }

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
  public boolean hasNoNext() {
    return true;
  }

  @Override
  public boolean hasNoPrevious() {
    return true;
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