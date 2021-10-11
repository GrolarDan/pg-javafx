package cz.masci.javafx.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class MainController {
  @FXML
  private TableView<Monster> tableView;
  
  @FXML
  private TableColumn<Monster, String> name;
  
  @FXML
  private TableColumn<Monster, String> description;

  @FXML
  private VBox details;
  
  // Name of this class has to be prefixed by the name of VBox variable. Means root node of the included fxml
  @FXML
  private DetailController detailsController;
          
  @FXML
  public void initialize() {
      tableView.setItems(getMonsters());
      name.setCellValueFactory(new PropertyValueFactory<>("name"));
      description.setCellValueFactory(new PropertyValueFactory<>("description"));
      
      tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        Monster monster = null;
        if (newValue != null) {
          monster = newValue;
        }
        detailsController.setMonster(monster);
      });
  }
  
  private ObservableList<Monster> getMonsters() {
    ObservableList<Monster> monsters = FXCollections.observableArrayList();
    for (int i = 0; i < 100; i++) {
      monsters.add(new Monster("Monster " + i, String.format("Description %d lorem ipsum", i)));
    }
    
    return monsters;
  }
}
