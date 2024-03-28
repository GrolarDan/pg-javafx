package cz.masci.javafx.demo.wizardtext.wizard.steps;

import cz.masci.javafx.demo.wizardtext.controller.HierarchicalStep;
import cz.masci.javafx.demo.wizardtext.controller.CompositeStep;
import cz.masci.javafx.demo.wizardtext.controller.Step;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

public class BaseCompositeStep implements CompositeStep {

  @Setter
  @Getter
  private HierarchicalStep parent;
  private final List<HierarchicalStep> steps = new ArrayList<>();

  @Getter
  private int currentIdx = -1;
  private CompositeStep currentChildIterator;
  private boolean doStep = false;

  @Override
  public Step prev() {
    // is already first step
    if (currentIdx < 0) {
      doStep = false;
      currentChildIterator = null;
      return applyOnIteratorStepOrNull(parent, CompositeStep::prev);
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
  public Step next() {
    // is already last step
    if (currentIdx >= steps.size()) {
      doStep = false;
      currentChildIterator = null;
      return applyOnIteratorStepOrNull(parent, CompositeStep::next);
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
  public boolean hasPrev() {
    return hasPrev(prepareChildIterator());
  }

  @Override
  public boolean hasNext() {
    return hasNext(prepareChildIterator());
  }

  @Override
  public String title() {
    if (prepareChildIterator()) {
      return currentChildIterator.title();
    }
    return "";
  }

  @Override
  public BooleanExpression valid() {
    return isValid(prepareChildIterator());
  }

  @Override
  public String prevText() {
    String text = null;
    if (prepareChildIterator()) {
      text = currentChildIterator.prevText();
      currentChildIterator = null;
    }
    if (text == null && currentIdx > 0) {
      text = getPrevText();
    }
    return text;
  }

  @Override
  public String nextText() {
    String text = null;
    if (prepareChildIterator()) {
      text = currentChildIterator.nextText();
      currentChildIterator = null;
    }
    if (text == null && currentIdx < steps.size() - 1) {
      text = getNextText();
    }
    return text;
  }

  public void addStep(HierarchicalStep step) {
    steps.add(step);
    step.setParent(this);
  }

  public String getNextText() {
    return "Next";
  }

  public String getPrevText() {
    return "Previous";
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
    return applyOnIteratorStepOrNull(parent, CompositeStep::hasPrev);
  }

  private boolean hasNext(boolean hasChildIterator) {
    if (hasChildIterator) {
      return currentChildIterator.hasNext() || currentIdx < steps.size();
    } else if (currentIdx < steps.size()) {
      currentChildIterator = null;
      return true;
    }
    return applyOnIteratorStepOrNull(parent, CompositeStep::hasNext);
  }

}
