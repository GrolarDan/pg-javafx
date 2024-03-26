package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.IteratorStep;
import cz.masci.javafx.demo.wizardtext.controller.LeafStep;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

public class Step_1_Iterator implements IteratorStep {

  private final static BooleanProperty TRUE_PROPERTY = new SimpleBooleanProperty(true);
  @Getter
  @Setter
  private IteratorStep parent;

  private final List<LeafStep<IteratorStep>> steps = new ArrayList<>();
  private int currentIdx = -1;
  private IteratorStep currentChildIterator;
  private boolean doStep = false;

  public Step_1_Iterator() {
  }

  public void addStep(LeafStep<IteratorStep> step) {
    steps.add(step);
    step.setParent(this);
  }

  public String getNextText() {
    return "Next Step 1";
  }

  public String getPrevText() {
    return "Previous Step 1";
  }

  @Override
  public LeafStep<IteratorStep> next() {
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
  public LeafStep<IteratorStep> prev() {
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

  @Override
  public String nextText() {
    String text = null;
    if (currentIdx < steps.size() - 1) {
      text = getNextText();
    } else if (parent != null) {
      text = parent.nextText();
    }
    return text;
  }

  @Override
  public String prevText() {
    String text = null;
    if (currentIdx > 0) {
      text = getPrevText();
    } else if (parent != null) {
      text = parent.prevText();
    }
    return text;
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
