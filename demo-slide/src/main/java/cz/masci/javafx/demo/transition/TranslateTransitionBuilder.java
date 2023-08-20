package cz.masci.javafx.demo.transition;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TranslateTransitionBuilder extends TransitionBuilder<TranslateTransition> {

  private double byX;
  private double byY;
  private double byZ;

  private TranslateTransitionBuilder() {
  }

  public static TranslateTransitionBuilder builder() {
    return new TranslateTransitionBuilder();
  }

  public TranslateTransitionBuilder withByX(double byX) {
    this.byX = byX;
    return this;
  }

  public TranslateTransitionBuilder withByY(double byY) {
    this.byY = byY;
    return this;
  }

  public TranslateTransitionBuilder withByZ(double byZ) {
    this.byZ = byZ;
    return this;
  }

  @Override
  protected TranslateTransition createTransition() {
    var transition = new TranslateTransition();
    transition.setByX(byX);
    transition.setByY(byY);
    transition.setByZ(byZ);

    return transition;
  }

  @Override
  protected void setDuration(TranslateTransition transition, Duration duration) {
    transition.setDuration(duration);
  }

  @Override
  protected void setNode(TranslateTransition transition, Node node) {
    transition.setNode(node);
  }
}
