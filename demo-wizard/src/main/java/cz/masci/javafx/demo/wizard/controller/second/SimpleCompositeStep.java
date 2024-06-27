package cz.masci.javafx.demo.wizard.controller.second;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

public abstract class SimpleCompositeStep implements CompositeStep {

  @Setter
  @Getter
  private HierarchicalStep parent;
  private final List<HierarchicalStep> steps = new ArrayList<>();

  @Getter
  private int currentIdx = -1;
  @Getter
  private boolean doStep = false;
  private CompositeStep currentChildIterator;

  protected abstract String getPrevText();

  protected abstract String getNextText();

  @Override
  public Step prev() {
    System.out.printf("Call prev(): %s, idx: %d, doStep: %s, childIterator: %s\n", getClass(), currentIdx, doStep, currentChildIterator);
    // is already first step
    if (currentIdx < 0) {
      doStep = false;
      currentChildIterator = null;
      return applyOnCompositeStepOr(parent, CompositeStep::prev, null);
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get()) {
      if (currentChildIterator != null && hasChildIterator) {
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
    System.out.printf("Call next(): %s, idx: %d, doStep: %s, childIterator: %s\n", getClass(), currentIdx, doStep, currentChildIterator);
    // is already last step
    if (currentIdx >= steps.size()) {
      doStep = false;
      currentChildIterator = null;
      return applyOnCompositeStepOr(parent, CompositeStep::next, null);
    }

    boolean hasChildIterator = prepareChildIterator();

    if (!hasChildIterator && doStep) {
      doStep = false;
      return steps.get(currentIdx);
    }

    if (isValid(hasChildIterator).get()) {
      if (currentChildIterator != null && hasChildIterator) {
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
  public Step goToStep(int index) {
    System.out.printf("Call goToStep(): %s, current idx: %d, new idx: %d\n", getClass(), currentIdx, index);
    if (!isValidIndex(index)) {
      return null;
    }

    currentIdx = index;

    if (prepareChildIterator()) {
      currentChildIterator.reset();
      var step = currentChildIterator.next();
      currentChildIterator = null;
      return step;
    }

    return steps.get(currentIdx);
  }

  @Override
  public void reset() {
    currentIdx = -1;
  }

  @Override
  public boolean hasPrev() {
    var result = hasPrev(prepareChildIterator());
    currentChildIterator = null;
    return result;
  }

  @Override
  public boolean hasNext() {
    var result = hasNext(prepareChildIterator());
    currentChildIterator = null;
    return result;
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

  public void clearSteps() {
    steps.clear();
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
    return isValidIndex(currentIdx) ? steps.get(currentIdx)
                                           .valid() : TRUE_PROPERTY;
  }

  private boolean hasPrev(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasPrev() || currentIdx > 0;
    } else if (currentIdx > 0) {
      currentChildIterator = null;
      return true;
    }
    return false;
  }

  private boolean hasNext(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasNext() || currentIdx < steps.size() - 1;
    } else if (currentIdx < steps.size() - 1) {
      currentChildIterator = null;
      return true;
    }
    return false;
  }
}
