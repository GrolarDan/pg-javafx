package cz.masci.javafx.demo.wizardtext;

import cz.masci.javafx.demo.wizardtext.wizard.Wizard;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_1_1;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_1_2;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_1_3;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_1_Composite;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_2;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_3;
import cz.masci.javafx.demo.wizardtext.wizard.steps.Step_0_Composite;

public class WizardTextApp {
  public static void main(String[] args) {
    var step1step2 = new Step_1_2();
    step1step2.setValid(false);

    var step1Iterator = new Step_1_Composite();
    step1Iterator.addStep(new Step_1_1());
    step1Iterator.addStep(step1step2);
    step1Iterator.addStep(new Step_1_3());

    var mainStep2 = new Step_2();
    mainStep2.setValid(false);

    var mainStep3 = new Step_3();
    mainStep3.setValid(false);

    var rootStep = new Step_0_Composite();

    rootStep.addStep(step1Iterator);
    rootStep.addStep(mainStep2);
    rootStep.addStep(mainStep3);

    var wizard = new Wizard(rootStep);

    wizard.next();  // 1-1
    wizard.next();  // 1-2
    wizard.next();  // 1-2 (not valid 1-2)
    step1step2.setValid(true);
    wizard.next();  // 1-3
    wizard.next();  // 2
    wizard.next();  // 2 (not valid 2)
    mainStep2.setValid(true);
    wizard.next();  // 3
    wizard.next();  // 3 (not valid 3)
    mainStep3.setValid(true);
    wizard.next();  // 3 (last item)
    wizard.prev();  // 2
    wizard.prev();  // 1-3
    wizard.prev();  // 1-2
    wizard.prev();  // 1-1
    wizard.prev();  // 1-1
    wizard.prev();  // 1-1
  }
}
