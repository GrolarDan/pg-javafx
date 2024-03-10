package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class WizardStepBuilder {

  private List<? extends WizardStep> children;
  private Consumer<WizardViewModel> beforeFirstNext; // on move from -1 to 0
  private Consumer<WizardViewModel> beforeFirstPrevious; // on move from size to size - 1
  private Consumer<WizardViewModel> afterLastNext; // on move from size-1 to size
  private Consumer<WizardViewModel> afterLastPrevious; // on move from 0 to -1
  private Consumer<StringProperty> updateTitle;  // call on next and previous
  private Consumer<StringProperty> updateNextText;
  private Consumer<StringProperty> updatePrevText;
  private Consumer<BooleanProperty> updateNextDisable;
  private Consumer<BooleanProperty> updatePrevDisable;

  private WizardStepBuilder() {
  }

  public static WizardStepBuilder builder() {
    return new WizardStepBuilder();
  }

  public LeafStep buildLeafStep(Builder<Region> viewBuilder) {
    LeafStep step = new LeafStep(viewBuilder.build());

    step.setUpdateTitle(updateTitle);
    step.setUpdateNextText(updateNextText);
    step.setUpdatePrevText(updatePrevText);
    step.setUpdateNextDisable(updateNextDisable);
    step.setUpdatePrevDisable(updatePrevDisable);

    return step;
  }

  public CompositeStep buildCompositeStep() {
    CompositeStep compositeStep = new CompositeStep();

    compositeStep.setBeforeFirstNext(beforeFirstNext);
    compositeStep.setBeforeFirstPrevious(beforeFirstPrevious);
    compositeStep.setAfterLastNext(afterLastNext);
    compositeStep.setAfterLastPrevious(afterLastPrevious);

    compositeStep.setUpdateTitle(updateTitle);
    compositeStep.setUpdateNextText(updateNextText);
    compositeStep.setUpdatePrevText(updatePrevText);
    compositeStep.setUpdateNextDisable(updateNextDisable);
    compositeStep.setUpdatePrevDisable(updatePrevDisable);

    compositeStep.setChildren(children);

    return compositeStep;
  }

  public RootCompositeStep buildRootCompositeStep() {
    RootCompositeStep compositeStep = new RootCompositeStep();

    compositeStep.setBeforeFirstNext(beforeFirstNext);
    compositeStep.setBeforeFirstPrevious(beforeFirstPrevious);
    compositeStep.setAfterLastNext(afterLastNext);
    compositeStep.setAfterLastPrevious(afterLastPrevious);

    compositeStep.setUpdateTitle(updateTitle);
    compositeStep.setUpdateNextText(updateNextText);
    compositeStep.setUpdatePrevText(updatePrevText);
    if (updateNextDisable != null) {
      compositeStep.setUpdateNextDisable(updateNextDisable);
    }
    if (updatePrevDisable != null) {
      compositeStep.setUpdatePrevDisable(updatePrevDisable);
    }

    compositeStep.setChildren(children);

    return compositeStep;
  }

  public WizardStepBuilder onBeforeFirstNext(Consumer<WizardViewModel> consumer) {
    this.beforeFirstNext = consumer;
    return this;
  }

  // on move from size to size - 1
  public WizardStepBuilder onBeforeFirstPrevious(Consumer<WizardViewModel> consumer) {
    beforeFirstPrevious = consumer;
    return this;
  }

  // on move from size-1 to size
  public WizardStepBuilder onAfterLastNext(Consumer<WizardViewModel> consumer) {
    afterLastNext = consumer;
    return this;
  }

  // on move from 0 to -1
  public WizardStepBuilder onAfterLastPrevious(Consumer<WizardViewModel> consumer) {
    afterLastPrevious = consumer;
    return this;
  }

  // call on next and previous
  public WizardStepBuilder updateTitle(Consumer<StringProperty> consumer) {
    updateTitle = consumer;
    return this;
  }

  public WizardStepBuilder updateNextText(Consumer<StringProperty> consumer) {
    updateNextText = consumer;
    return this;
  }

  public WizardStepBuilder updatePrevText(Consumer<StringProperty> consumer) {
    updatePrevText = consumer;
    return this;
  }

  public WizardStepBuilder updateNextDisable(Consumer<BooleanProperty> consumer) {
    updateNextDisable = consumer;
    return this;
  }

  public WizardStepBuilder updatePrevDisable(Consumer<BooleanProperty> consumer) {
    updatePrevDisable = consumer;
    return this;
  }

  public WizardStepBuilder children(List<? extends WizardStep> children) {
    this.children = children;
    return this;
  }
}
