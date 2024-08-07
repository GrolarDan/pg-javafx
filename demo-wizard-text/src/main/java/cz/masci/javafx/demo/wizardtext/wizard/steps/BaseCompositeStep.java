package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.HierarchicalStep;
import cz.masci.javafx.demo.wizardtext.controller.CompositeStep;
import cz.masci.javafx.demo.wizardtext.controller.Step;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseCompositeStep implements CompositeStep {

  @Setter
  @Getter
  private HierarchicalStep parent;
  private final List<HierarchicalStep> steps = new ArrayList<>();

  @Getter
  private int currentIdx = -1;
  private CompositeStep currentChildIterator;
  private boolean doStep = false;

  protected abstract String getPrevText();
  protected abstract String getNextText();

  @Override
  public Step prev() {
//    System.out.printf("Call prev(): %s, idx: %d, doStep: %s, childIterator: %s\n", getClass(), currentIdx, doStep, currentChildIterator);
    // is already first step
    if (currentIdx < 0) {
      doStep = false;
      currentChildIterator = null;
      return applyOnCompositeStepOrNull(parent, CompositeStep::prev);
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get() && hasPrev(hasChildIterator)) {
      if (currentChildIterator != null) {
        var prevStep = currentChildIterator.prev();
        doStep = false;
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
  public Step next() {
//    System.out.printf("Call next(): %s, idx: %d, doStep: %s, childIterator: %s\n", getClass(), currentIdx, doStep, currentChildIterator);
    // is already last step
    if (currentIdx >= steps.size()) {
      doStep = false;
      currentChildIterator = null;
      return applyOnCompositeStepOrNull(parent, CompositeStep::next);
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get() && hasNext(hasChildIterator)) {
      if (currentChildIterator != null) {
        var nextStep = currentChildIterator.next();
        doStep = false;
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
  public boolean hasPrev() {
    return hasPrev(prepareChildIterator());
  }

  @Override
  public boolean hasNext() {
    return hasNext(prepareChildIterator());
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

  public void addStep(HierarchicalStep step) {
    steps.add(step);
    step.setParent(this);
  }

  protected boolean isValidIndex(int index) {
    return index >= 0 && index < steps.size();
  }

  private boolean prepareChildIterator() {
    if (isValidIndex(currentIdx) && steps.get(currentIdx) instanceof CompositeStep compositeStep && (currentChildIterator == null || !currentChildIterator.equals(compositeStep))) {
      currentChildIterator = compositeStep;
      return true;
    } else {
      return false;
    }
  }

  private BooleanExpression isValid(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.valid();
    }
    return isValidIndex(currentIdx) ? steps.get(currentIdx).valid() : TRUE_PROPERTY;
  }

  private boolean hasPrev(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasPrev() || currentIdx >= 0;
    } else if (currentIdx >= 0) {
      currentChildIterator = null;
      return true;
    }
    return applyOnCompositeStepOrNull(parent, CompositeStep::hasPrev);
  }

  private boolean hasNext(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasNext() || currentIdx < steps.size();
    } else if (currentIdx < steps.size()) {
      currentChildIterator = null;
      return true;
    }
    return applyOnCompositeStepOrNull(parent, CompositeStep::hasNext);
  }

}
