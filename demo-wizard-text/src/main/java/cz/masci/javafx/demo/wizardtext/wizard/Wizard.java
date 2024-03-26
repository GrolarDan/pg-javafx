package cz.masci.javafx.demo.wizardtext.wizard;

import cz.masci.javafx.demo.wizardtext.controller.IteratorStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Wizard {

  private final IteratorStep iteratorStep;

  public void next() {
    var step = iteratorStep.next();
    if (step != null) {
      printStepDescription(step, "NEXT");
    }
  }

  public void prev() {
    var step = iteratorStep.prev();
    if (step != null) {
      printStepDescription(step, "PREV");
    }
  }

  private void printStepDescription(LeafStep<?> step, String direction) {
    System.out.println("==============================");
    System.out.printf("Moved to %s step\n", direction);
    System.out.println("\tStep INFO:");
    System.out.printf("\t\tTITLE: %s\n", step.title());
    System.out.printf("\t\tPREV TEXT: %s\n", step.prevText());
    System.out.printf("\t\tNEXT TEXT: %s\n", step.nextText());
//    System.out.printf("\t\tNEXT DISABLED: %s\n", step.nextDisabled().get());
//    System.out.printf("\t\tPREV DISABLED: %s\n", step.prevDisabled().get());
    System.out.printf("\t\tIS VALID: %s\n", step.isValid() != null ? step.isValid().get() : "N/A");
  }
}
