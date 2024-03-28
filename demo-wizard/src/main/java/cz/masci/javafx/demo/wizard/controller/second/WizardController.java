package cz.masci.javafx.demo.wizard.controller.second;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import cz.masci.javafx.demo.wizard.view.WizardViewBuilder;
import java.util.Optional;
import javafx.scene.layout.Region;

public class WizardController {

  private final WizardViewBuilder builder;
  private final IteratorStep root;
  private final WizardViewModel wizardViewModel;

  public WizardController(IteratorStep root) {
    wizardViewModel = new WizardViewModel();
    builder = new WizardViewBuilder(this::getPrevView, this::getNextView, wizardViewModel);
    this.root = root;
  }

  public Region getView() {
    LeafStep step = root.next();
    updateWizardViewModel(step);
    return builder.build(step.view());
  }

  private Optional<Region> getNextView() {
    LeafStep step = root.next();
    updateWizardViewModel(step);
    return Optional.ofNullable(step.view());
  }

  private Optional<Region> getPrevView() {
    LeafStep step = root.prev();
    updateWizardViewModel(step);
    return Optional.ofNullable(step.view());
  }

  private void updateWizardViewModel(LeafStep step) {
    wizardViewModel.titleProperty().set(step.title());
    wizardViewModel.prevTextProperty().set(root.prevText());
    if (wizardViewModel.prevDisableProperty().isBound()) {
      wizardViewModel.prevDisableProperty().unbind();
    }
    wizardViewModel.prevDisableProperty().bind(step.isValid());
    wizardViewModel.nextTextProperty().set(root.nextText());
    if (wizardViewModel.nextDisableProperty().isBound()) {
      wizardViewModel.nextDisableProperty().unbind();
    }
    wizardViewModel.nextDisableProperty().bind(step.isValid());
  }
}
