package cz.masci.javafx.demo.wizardtext.wizard;

import cz.masci.javafx.demo.wizardtext.controller.IteratorStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Wizard {

  private final IteratorStep root;

  public void next() {
    var step = root.next();
    if (step != null) {
      printStepDescription(step, "NEXT");
    } else {
      System.out.println("Waiting for valid step");
    }
  }

  public void prev() {
    var step = root.prev();
    if (step != null) {
      printStepDescription(step, "PREV");
    } else {
      System.out.println("Waiting for valid step");
    }
  }

  private void printStepDescription(LeafStep step, String direction) {
    System.out.println("==============================");
    System.out.printf("Moved to %s step\n", direction);
    System.out.println("\tStep INFO:");
    System.out.printf("\t\tTITLE: %s\n", step.title());
    System.out.printf("\t\tPREV TEXT: %s\n", root.prevText());
    System.out.printf("\t\tNEXT TEXT: %s\n", root.nextText());
//    System.out.printf("\t\tNEXT DISABLED: %s\n", step.nextDisabled().get());
//    System.out.printf("\t\tPREV DISABLED: %s\n", step.prevDisabled().get());
    System.out.printf("\t\tIS VALID: %s\n", step.isValid() != null ? step.isValid().get() : "N/A");
  }
}
