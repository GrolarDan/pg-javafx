package cz.masci.javafx.demo.transition;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeTransitionBuilder extends TransitionBuilder<FadeTransition> {

  private double fromValue;
  private double toValue;

  private FadeTransitionBuilder() {
  }

  public static FadeTransitionBuilder builder() {
    return new FadeTransitionBuilder();
  }

  public FadeTransitionBuilder withFromValue(double fromValue) {
    this.fromValue = fromValue;
    return this;
  }

  public FadeTransitionBuilder withToValue(double toValue) {
    this.toValue = toValue;
    return this;
  }

  @Override
  protected FadeTransition createTransition() {
    var transition = new FadeTransition();
    transition.setFromValue(fromValue);
    transition.setToValue(toValue);

    return transition;
  }

  @Override
  protected void setDuration(FadeTransition transition, Duration duration) {
    transition.setDuration(duration);
  }

  @Override
  protected void setNode(FadeTransition transition, Node node) {
    transition.setNode(node);
  }
}
