package cz.masci.javafx.demo;

import cz.masci.javafx.demo.transition.TranslateTransitionBuilder;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlideService {
  @Getter
  private final ObservableList<Node> nodeList = FXCollections.observableArrayList();
  private final ReadOnlyDoubleProperty widthProperty;
  private Node currentNode;
  private Integer currentIndex;

  public SlideService(@NonNull ReadOnlyDoubleProperty widthProperty) {
    this.widthProperty = widthProperty;
    nodeList.addListener(updateCurrentNode());
  }

  public void previous() {
    // Check currentIndex
    if (currentIndex == null || currentIndex == 0) {
      log.warn("Can't slide previous because it is already first node");
      return;
    }
    // Transform previous node to the left
    Node previousNode = nodeList.get(currentIndex - 1);
    previousNode.translateXProperty().set(-widthProperty.doubleValue());
    previousNode.setVisible(true);
    // Create translate transition for previous node from left to right
    var previousNodeTransition = TranslateTransitionBuilder.builder()
        .withByX(widthProperty.doubleValue())
        .withDuration(Duration.millis(500))
        .withNode(previousNode)
        .build();
    // Create translate transition for current node from left to right
    var currentNodeTransition = TranslateTransitionBuilder.builder()
        .withByX(widthProperty.doubleValue())
        .withDuration(Duration.millis(500))
        .withNode(currentNode)
        .withOnFinishedHandler(event -> {
          currentNode.setVisible(false);
          currentNode.translateXProperty().set(-widthProperty.doubleValue());
          currentNode = previousNode;
          currentIndex -= 1;
        })
        .build();
    previousNodeTransition.play();
    currentNodeTransition.play();
  }

  public void next() {
    // Check currentIndex
    if (currentIndex == null || currentIndex == nodeList.size() - 1) {
      log.warn("Can't slide previous because it is already last node");
      return;
    }
    // Transform next node to the right
    Node nextNode = nodeList.get(currentIndex + 1);
    nextNode.translateXProperty().set(widthProperty.doubleValue());
    nextNode.setVisible(true);
    // Create translate transition for previous node from right to left
    var nextNodeTransition = TranslateTransitionBuilder.builder()
        .withByX(-widthProperty.doubleValue())
        .withDuration(Duration.millis(500))
        .withNode(nextNode)
        .build();
    // Create translate transition for current node from right to left
    var currentNodeTransition = TranslateTransitionBuilder.builder()
        .withByX(-widthProperty.doubleValue())
        .withDuration(Duration.millis(500))
        .withNode(currentNode)
        .withOnFinishedHandler(event -> {
          currentNode.setVisible(false);
          currentNode.translateXProperty().set(widthProperty.doubleValue());
          currentNode = nextNode;
          currentIndex += 1;
        })
        .build();
    nextNodeTransition.play();
    currentNodeTransition.play();
  }

  public void set(int index) {
    // Based on current index transform from right to left when index > currentIndex and from left to right when index < currentIndex
  }

  /**
   * Set or unset current node based on node list items change.
   *
   * @return ListChangeListener
   */
  private ListChangeListener<? super Node> updateCurrentNode() {
    return change -> {
      if (currentNode == null && nodeList.size() > 0) {
        currentNode = nodeList.get(0);
        currentIndex = 0;
      }
      if (currentNode != null && nodeList.size() == 0) {
        currentNode = null;
        currentIndex = null;
      }
    };
  }


}
