package cz.masci.javafx.demo;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class SlideController {

  private static final double TRANSITION_DURATION = 0.5;

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

  private SlideService slideService;

  public void initialize() {
    System.out.println("Initialize");

    centerPane.setOnSwipeLeft(this::onNext);
    centerPane.setOnSwipeRight(this::onPrevious);

    grid1.setVisible(true);
    grid2.setVisible(false);

    slideService = new SlideService(mainPane.widthProperty());
    slideService.getNodeList().add(grid1);
    slideService.getNodeList().add(grid2);
  }

  @FXML
  private void onNext(Event event) {
    System.out.println("onNext");
    slideService.next();
  }

  @FXML
  private void onPrevious(Event event) {
    System.out.println("onPrevious");
    slideService.previous();
  }
}
