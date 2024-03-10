package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompositeStep implements WizardStep {
  private final static int BEFORE_INTERVAL = -1;
  protected WizardStep currentStep;
  private int currentPosition = BEFORE_INTERVAL;

  @Setter
  private List<? extends WizardStep> children;

  @Setter
  private Consumer<WizardViewModel> beforeFirstNext; // on move from -1 to 0
  @Setter
  private Consumer<WizardViewModel> beforeFirstPrevious; // on move from size to size - 1
  @Setter
  private Consumer<WizardViewModel> afterLastNext; // on move from size-1 to size
  @Setter
  private Consumer<WizardViewModel> afterLastPrevious; // on move from 0 to -1
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
    if (currentPosition == BEFORE_INTERVAL && beforeFirstNext != null) {
      beforeFirstNext.accept(wizardViewModel);
    }

    if (children == null) {
      throw new ArrayStoreException("Children has to be initialized");
    }

    doStep(currentPosition + 1, wizardViewModel);

    if (currentPosition > children.size() && afterLastNext != null) {
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

    if (currentPosition > children.size() && beforeFirstPrevious != null) {
      beforeFirstPrevious.accept(wizardViewModel);
    }

    doStep(currentPosition - 1, wizardViewModel);

    if (currentPosition > children.size() && afterLastPrevious != null) {
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
  public boolean hasNext() {
    return currentPosition < children.size() - 1;
  }

  @Override
  public boolean hasPrevious() {
    return currentPosition > 0;
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
    if (Optional.ofNullable(currentStep).map(WizardStep::current).isEmpty()) {  // current step is empty/null
      currentStep = -1 < newPosition && newPosition < children.size() ? children.get(newPosition) : null;
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
    consumeIfNotNullOrElseConsume(updateNextText, this::defaultNextText, wizardViewModel.nextTextProperty());
    consumeIfNotNullOrElseConsume(updatePrevText, this::defaultPrevText, wizardViewModel.prevTextProperty());
    consumeIfNotNullOrElseConsume(updateNextDisable, this::defaultNextDisable, wizardViewModel.nextDisableProperty());
    consumeIfNotNullOrElseConsume(updatePrevDisable, this::defaultPrevDisable, wizardViewModel.prevDisableProperty());
  }

  private void unbindWizardViewModel(WizardViewModel wizardViewModel) {
    unbindIfBound(wizardViewModel.titleProperty());
    unbindIfBound(wizardViewModel.nextTextProperty());
    unbindIfBound(wizardViewModel.prevTextProperty());
    unbindIfBound(wizardViewModel.nextDisableProperty());
    unbindIfBound(wizardViewModel.prevDisableProperty());
  }

  private void defaultNextText(StringProperty nextText) {
    nextText.set("Další");
  }

  private void defaultPrevText(StringProperty prevText) {
    prevText.set("Předchozí");
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

  private <T> void consumeIfNotNullOrElseConsume(Consumer<T> consumer, Consumer<T> elseConsumer, T value) {
    if (consumer != null) {
      consumer.accept(value);
    } else {
      elseConsumer.accept(value);
    }
  }

  private <T extends Property<?>> void unbindIfBound(T property) {
    if (property.isBound()) {
      property.unbind();
    }
  }
}
