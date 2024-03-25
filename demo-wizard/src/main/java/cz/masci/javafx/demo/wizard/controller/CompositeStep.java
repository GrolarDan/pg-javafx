package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompositeStep implements WizardStep {
  // TODO Change next/prevText based on step
  private final static int BEFORE_INTERVAL = -1;
  protected WizardStep currentStep;
  private int currentPosition = BEFORE_INTERVAL;

  @Setter
  private List<? extends WizardStep> children;

  @Getter
  private final String name;
  private final Consumer<WizardViewModel> beforeFirstNext; // on move from -1 to 0
  private final Consumer<WizardViewModel> beforeFirstPrevious; // on move from size to size - 1
  private final Consumer<WizardViewModel> afterLastNext; // on move from size-1 to size
  private final Consumer<WizardViewModel> afterLastPrevious; // on move from 0 to -1
  private final Consumer<StringProperty> updateTitle;
  private final Consumer<StringProperty> updateNextText;
  private final Consumer<StringProperty> updatePrevText;
  private final Consumer<BooleanProperty> updateNextDisable;
  private final Consumer<BooleanProperty> updatePrevDisable;

  @Builder
  public CompositeStep(String name, List<? extends WizardStep> children, Consumer<WizardViewModel> beforeFirstNext, Consumer<WizardViewModel> beforeFirstPrevious,
                       Consumer<WizardViewModel> afterLastNext, Consumer<WizardViewModel> afterLastPrevious, Consumer<StringProperty> updateTitle,
                       Consumer<StringProperty> updateNextText, Consumer<StringProperty> updatePrevText, Consumer<BooleanProperty> updateNextDisable,
                       Consumer<BooleanProperty> updatePrevDisable) {
    this.name = name;
    this.children = children;
    this.beforeFirstNext = beforeFirstNext;
    this.beforeFirstPrevious = beforeFirstPrevious;
    this.afterLastNext = afterLastNext;
    this.afterLastPrevious = afterLastPrevious;
    this.updateTitle = updateTitle;
    this.updateNextText = Objects.requireNonNullElse(updateNextText, this::defaultNextText);
    this.updatePrevText = Objects.requireNonNullElse(updatePrevText, this::defaultPrevText);
    this.updateNextDisable = Objects.requireNonNullElse(updateNextDisable, this::defaultNextDisable);
    this.updatePrevDisable = Objects.requireNonNullElse(updatePrevDisable, this::defaultPrevDisable);
  }

  @Override
  public void next(WizardViewModel wizardViewModel) {
    if (currentPosition == BEFORE_INTERVAL && beforeFirstNext != null) {
      beforeFirstNext.accept(wizardViewModel);
    }

    if (children == null) {
      throw new ArrayStoreException("Children has to be initialized");
    }

    doStep(currentPosition + 1, wizardViewModel);

    if (currentPosition == children.size() && afterLastNext != null) {
      // the position could be changed
      afterLastNext.accept(wizardViewModel);
    }

    if (currentPosition == children.size()) {
      unbindWizardViewModel(wizardViewModel);
    }
  }

  @Override
  public void previous(WizardViewModel wizardViewModel) {
    if (children == null) {
      throw new ArrayStoreException("Children has to be initialized");
    }

    if (currentPosition == children.size() && beforeFirstPrevious != null) {
      beforeFirstPrevious.accept(wizardViewModel);
    }

    doStep(currentPosition - 1, wizardViewModel);

    if (currentPosition == BEFORE_INTERVAL && afterLastPrevious != null) {
      // the position could be changed
      afterLastPrevious.accept(wizardViewModel);
    }

    if (currentPosition == BEFORE_INTERVAL) {
      unbindWizardViewModel(wizardViewModel);
    }
  }

  @Override
  public WizardStep current() {
    return currentStep;
  }

  @Override
  public Region getView() {
    return currentStep != null ? currentStep.getView() : null;
  }

  @Override
  public boolean hasNoNext() {
    return currentPosition >= children.size() - 1 && (currentStep == null || currentStep.hasNoNext());
  }

  @Override
  public boolean hasNoPrevious() {
    return currentPosition <= 0 && (currentStep == null || current().hasNoPrevious());
  }

  @Override
  public void reset() {
    currentPosition = BEFORE_INTERVAL;
    currentStep = null;
  }

  public void goToPosition(int newPosition, WizardViewModel wizardViewModel) {
    if (newPosition == 0 && beforeFirstNext != null) {
      beforeFirstNext.accept(wizardViewModel);
    }

    if (newPosition == children.size() -1 && beforeFirstPrevious != null) {
      beforeFirstPrevious.accept(wizardViewModel);
    }

    currentStep = newPosition > -1 && newPosition < children.size() ? children.get(newPosition) : null;

    if (currentStep != null) {
      currentStep.reset();
      currentStep.next(wizardViewModel);
    }

    currentPosition = newPosition;

    updateWizardViewModel(wizardViewModel);
  }

  private void doStep(int newPosition, WizardViewModel wizardViewModel) {
    // do prev/next on current step
    if (currentStep != null) {
      if (newPosition > currentPosition) {
        currentStep.next(wizardViewModel);
      } else {
        currentStep.previous(wizardViewModel);
      }
    }
    if (Optional.ofNullable(currentStep)
                .map(WizardStep::current)
                .isEmpty()) {  // current step is empty/null
      currentStep = newPosition > -1 && newPosition < children.size() ? children.get(newPosition) : null;
      if (currentStep != null) { // if there is still some step in this composite do the prev/next on the child
        if (newPosition > currentPosition) {
          currentStep.next(wizardViewModel);
        } else {
          currentStep.previous(wizardViewModel);
        }
      }
      currentPosition = newPosition;
    }

    updateWizardViewModel(wizardViewModel);
  }

  private void updateWizardViewModel(WizardViewModel wizardViewModel) {
    consumeIfNotNull(updateTitle, wizardViewModel.titleProperty());
    updateNextText.accept(wizardViewModel.nextTextProperty());
    updatePrevText.accept(wizardViewModel.prevTextProperty());
    updateNextDisable.accept(wizardViewModel.nextDisableProperty());
    updatePrevDisable.accept(wizardViewModel.prevDisableProperty());
  }

  private void unbindWizardViewModel(WizardViewModel wizardViewModel) {
    unbindIfBound(wizardViewModel.titleProperty());
    unbindIfBound(wizardViewModel.nextTextProperty());
    unbindIfBound(wizardViewModel.prevTextProperty());
    unbindIfBound(wizardViewModel.nextDisableProperty());
    unbindIfBound(wizardViewModel.prevDisableProperty());
  }

  private void defaultNextText(StringProperty nextText) {
    if (hasNoNext()) {
      nextText.set("Další");
    }
  }

  private void defaultPrevText(StringProperty prevText) {
    if (hasNoPrevious()) {
      prevText.set("Předchozí");
    }
  }

  private void defaultNextDisable(BooleanProperty nextDisable) {
    if (!nextDisable.isBound()) {
      nextDisable.set(false);
    }
  }

  private void defaultPrevDisable(BooleanProperty prevDisable) {
    if (!prevDisable.isBound()) {
      prevDisable.set(false);
    }
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
