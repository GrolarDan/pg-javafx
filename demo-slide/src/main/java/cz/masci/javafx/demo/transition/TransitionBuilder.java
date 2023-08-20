package cz.masci.javafx.demo.transition;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class TransitionBuilder<T extends Transition> {
  private Duration duration;
  private Node node;
  private EventHandler<ActionEvent> onFinishedHandler;

  protected TransitionBuilder() {
  }

  // region builder

  public TransitionBuilder<T> withNode(Node node) {
    this.node = node;
    return this;
  }

  public TransitionBuilder<T> withDuration(Duration duration) {
    this.duration = duration;
    return this;
  }

  public TransitionBuilder<T> withOnFinishedHandler(EventHandler<ActionEvent> onFinishedHandler) {
    this.onFinishedHandler = onFinishedHandler;
    return this;
  }

  public T build() {
    var transition = createTransition();
    Optional.ofNullable(duration).ifPresent(setTransitionValue(transition, this::setDuration));
    Optional.ofNullable(node).ifPresent(setTransitionValue(transition, this::setNode));
    Optional.ofNullable(onFinishedHandler).ifPresent(transition::setOnFinished);

    return transition;
  }

  // endregion

  protected abstract T createTransition();

  protected abstract void setDuration(T transition, Duration duration);

  protected abstract void setNode(T transition, Node node);

  private <V> Consumer<V> setTransitionValue(T transition, BiConsumer<T, V> valueSetter) {
    return (value) -> valueSetter.accept(transition, value);
  }
}
