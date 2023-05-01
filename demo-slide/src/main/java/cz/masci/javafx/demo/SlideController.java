package cz.masci.javafx.demo;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class SlideController {

  private static final double TRANSITION_DURATION = 0.5;

  private Node currentNode;

  @FXML
  private AnchorPane centerPane;
  @FXML
  private BorderPane mainPane;
  @FXML
  private Button previousBtn;
  @FXML
  private Button nextBtn;
  @FXML
  private GridPane grid1;
  @FXML
  private GridPane grid2;

  public void initialize() {
    System.out.println("Initialize");

    mainPane.widthProperty().addListener(this::onMainPaneWidthChange);
    centerPane.setOnSwipeLeft(this::onSwipeNext);
    centerPane.setOnSwipeRight(this::onSwipePrevious);
//    grid1.onSwipeLeftProperty().addListener(this::onSwipeNext);
//    grid1.onSwipeRightProperty().addListener(this::onSwipePrevious);
//    grid2.onSwipeLeftProperty().addListener(this::onSwipeNext);
//    grid2.onSwipeRightProperty().addListener(this::onSwipePrevious);

    previousBtn.disableProperty().set(true);
    nextBtn.disableProperty().set(false);

    grid1.setVisible(true);
    grid2.setVisible(true);

    currentNode = grid1;
  }

  @FXML
  private void onNext(ActionEvent event) {
    System.out.println("onNext");
    swipeNext();
  }

  private void onSwipeNext(SwipeEvent event) {
    System.out.println("onSwipeNext");
    swipeNext();
  }

  @FXML
  private void onPrevious(ActionEvent event) {
    System.out.println("onPrevious");
    swipePrevious();
  }

  private void onSwipePrevious(SwipeEvent event) {
    System.out.println("onSwipePrevious");
    swipePrevious();
  }

  private void swipePrevious() {
    System.out.println("swipePrevious");

    if (currentNode != grid2) {
      return;
    }

    TranslateTransition translateTransitionPrevious = new TranslateTransition(Duration.seconds(TRANSITION_DURATION), grid1);
    translateTransitionPrevious.setByX(mainPane.getWidth());
    translateTransitionPrevious.play();

    TranslateTransition translateTransitionCurrent = new TranslateTransition(Duration.seconds(TRANSITION_DURATION), grid2);
    translateTransitionCurrent.setByX(mainPane.getWidth());
    translateTransitionCurrent.play();

    previousBtn.disableProperty().set(true);
    nextBtn.disableProperty().set(false);

    currentNode = grid1;
  }

  private void swipeNext() {
    System.out.println("swipeNext");

    if (currentNode != grid1) {
      return;
    }

    TranslateTransition translateTransitionNext = new TranslateTransition(Duration.seconds(TRANSITION_DURATION), grid2);
    translateTransitionNext.setByX(-mainPane.getWidth());
    translateTransitionNext.play();

    TranslateTransition translateTransitionCurrent = new TranslateTransition(Duration.seconds(TRANSITION_DURATION), grid1);
    translateTransitionCurrent.setByX(-mainPane.getWidth());
    translateTransitionCurrent.play();

    //    grid1.setVisible(false);
    previousBtn.disableProperty().set(false);
    nextBtn.disableProperty().set(true);

    currentNode = grid2;
  }

  private void onMainPaneWidthChange(ObservableValue<? extends Number> obsarvable, Number oldValue, Number newValue) {
    System.out.println("Border main pane width changed to " + newValue.toString());
    if (currentNode != grid1) {
      grid1.translateXProperty().set(-newValue.doubleValue());
    }
    if (currentNode != grid2) {
      grid2.translateXProperty().set(newValue.doubleValue());
    }
  }
}
