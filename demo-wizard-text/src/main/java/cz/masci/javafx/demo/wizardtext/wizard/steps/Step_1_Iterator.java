package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.IteratorStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Setter;

public class Step_1_Iterator implements IteratorStep {

  private final static BooleanProperty TRUE_PROPERTY = new SimpleBooleanProperty(true);
  @Setter
  private IteratorStep parent;

  private final List<LeafStep> steps = new ArrayList<>();
  private int currentIdx = -1;
  private IteratorStep currentChildIterator;
  private boolean doStep = false;

  public Step_1_Iterator() {
  }

  public void addStep(LeafStep step) {
    steps.add(step);
    if (step instanceof IteratorStep iteratorStep) {
      iteratorStep.setParent(this);
    }
  }

  @Override
  public LeafStep next() {
    // is already last step
    if (currentIdx >= steps.size()) {
      doStep = false;
      currentChildIterator = null;
      return parent != null ? parent.next() : null;
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get() && hasNext(hasChildIterator)) {
      if (currentChildIterator != null) {
        var nextStep = currentChildIterator.next();
        currentChildIterator = null;
        return nextStep;
      } else {
        currentIdx++;
        doStep = true;
        return next();
      }
    }

    doStep = false;
    currentChildIterator = null;
    return null;
  }

  @Override
  public boolean hasNext() {
    return hasNext(prepareChildIterator());
  }

  @Override
  public LeafStep prev() {
    // is already first step
    if (currentIdx < 0) {
      doStep = false;
      currentChildIterator = null;
      return parent != null ? parent.prev() : null;
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get() && hasPrev(hasChildIterator)) {
      if (currentChildIterator != null) {
        var prevStep = currentChildIterator.prev();
        currentChildIterator = null;
        return prevStep;
      } else {
        currentIdx--;
        doStep = true;
        return prev();
      }
    }

    doStep = false;
    currentChildIterator = null;
    return null;
  }

  @Override
  public boolean hasPrev() {
    return hasPrev(prepareChildIterator());
  }

  @Override
  public String title() {
    if (prepareChildIterator()) {
      return currentChildIterator.title();
    }
    return "";
  }

  @Override
  public BooleanExpression isValid() {
    return isValid(prepareChildIterator());
  }

  protected boolean isValidIndex(int index) {
    return index >= 0 && index < steps.size();
  }

  private boolean prepareChildIterator() {
    if (isValidIndex(currentIdx) && steps.get(currentIdx) instanceof IteratorStep iteratorStep && (currentChildIterator == null || !currentChildIterator.equals(iteratorStep))) {
      currentChildIterator = iteratorStep;
      return true;
    } else {
      return false;
    }
  }

  private BooleanExpression isValid(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.isValid();
    }
    return isValidIndex(currentIdx) ? steps.get(currentIdx).isValid() : TRUE_PROPERTY;
  }

  private boolean hasNext(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasNext() || currentIdx < steps.size();
    } else if (currentIdx < steps.size()) {
      currentChildIterator = null;
      return true;
    }
    return parent != null && parent.hasNext();
  }

  private boolean hasPrev(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasPrev() || currentIdx >= 0;
    } else if (currentIdx >= 0) {
      currentChildIterator = null;
      return true;
    }
    return parent != null && parent.hasPrev();
  }
}
