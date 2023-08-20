package cz.masci.javafx.demo.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.Transition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransitionPlayer {

  private final List<TransitionBuilder<?>> builderList = new ArrayList<>();
  private Transition firstTransition;
  private Transition lastTransition;

  public <T extends Transition> void add(TransitionBuilder<T> builder) {
    log.debug("Added builder: {}", builder);
    builderList.add(builder);
  }

  public void play() {
    log.debug("Start play - size: {}", builderList.size());
    build();
    getFirstTransition().ifPresent(Transition::play);
  }

  public void playInfinite() {
    log.debug("Start play infinite - size: {}", builderList.size());
    build();
    if (getFirstTransition().isPresent() && getLastTransition().isPresent()) {
      lastTransition.setOnFinished(actionEvent -> firstTransition.play());
      firstTransition.play();
    }
  }

  public Optional<Transition> getFirstTransition() {
    return Optional.ofNullable(firstTransition);
  }

  public Optional<Transition> getLastTransition() {
    return Optional.ofNullable(lastTransition);
  }

  private void build() {
    if (builderList.isEmpty()) {
      return;
    }

    build(builderList.get(0), 1);
  }

  private Transition build(TransitionBuilder<?> current, int nextIndex) {
    if (nextIndex < builderList.size()) {
      var nextTransitionBuild = build(builderList.get(nextIndex), nextIndex + 1);
      current.withOnFinishedHandler(actionEvent -> nextTransitionBuild.play());
    }
    var transition = current.build();
    // set last transition
    if (nextIndex == builderList.size()) {
      lastTransition = transition;
    }
    // set first transition
    if (nextIndex == 1) {
      firstTransition = transition;
    }
    // return transition
    return transition;
  }
}
