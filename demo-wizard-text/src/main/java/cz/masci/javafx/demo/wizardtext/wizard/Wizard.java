package cz.masci.javafx.demo.wizardtext.wizard;

import cz.masci.javafx.demo.wizardtext.controller.CompositeStep;
import cz.masci.javafx.demo.wizardtext.controller.Step;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Wizard {

  private final CompositeStep root;

  public void next() {
    var step = root.next();
    if (step != null) {
      printStepDescription(step, "NEXT");
    } else {
      System.out.println("==============================");
      System.out.println("Waiting for valid step");
    }
  }

  public void prev() {
    var step = root.prev();
    if (step != null) {
      printStepDescription(step, "PREV");
    } else {
          System.out.println("==============================");
      System.out.println("Waiting for valid step");
    }
  }

  private void printStepDescription(Step step, String direction) {
    System.out.println("==============================");
    System.out.printf("Moved to %s step\n", direction);
    System.out.println("\tStep INFO:");
    System.out.printf("\t\tinstance: %s\n", step.getClass().toGenericString());
    System.out.printf("\t\tTITLE: %s\n", step.title());
    System.out.printf("\t\tPREV TEXT: %s\n", step.prevText());
    System.out.printf("\t\tNEXT TEXT: %s\n", step.nextText());
    System.out.printf("\t\tIS VALID: %s\n", step.valid() != null ? step.valid().get() : "N/A");
  }
}
