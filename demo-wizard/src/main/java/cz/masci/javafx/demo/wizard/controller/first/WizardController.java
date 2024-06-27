package cz.masci.javafx.demo.wizard.controller.first;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import cz.masci.javafx.demo.wizard.view.WizardViewBuilder;
import java.util.Optional;
import javafx.scene.layout.Region;

public class WizardController {

  private final WizardViewBuilder builder;
  private final WizardStepProvider stepProvider;
  private final WizardViewModel wizardViewModel;

  public WizardController(WizardStepProvider stepProvider) {
    wizardViewModel = new WizardViewModel();
    builder = new WizardViewBuilder(this::getPrevView, this::getNextView, wizardViewModel);
    this.stepProvider = stepProvider;
  }

  public Region getView() {
    var step = stepProvider.getWizardStep();
    step.next(wizardViewModel);
    return builder.build(step.getView());
  }

  private Optional<Region> getNextView() {
    var step = stepProvider.getWizardStep();
    step.next(wizardViewModel);
    return Optional.ofNullable(step.getView());
  }

  private Optional<Region> getPrevView() {
    var step = stepProvider.getWizardStep();
    step.previous(wizardViewModel);
    return Optional.ofNullable(step.getView());
  }
}
