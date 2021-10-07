package cz.masci.javafx.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
  @FXML
  private TableView<Monster> tableView;
  
  @FXML
  private TableColumn<Monster, String> name;
  
  @FXML
  private TableColumn<Monster, String> description;

  @FXML
  public void initialize() {
      tableView.setItems(getMonsters());
      name.setCellValueFactory(new PropertyValueFactory<>("name"));
      description.setCellValueFactory(new PropertyValueFactory<>("description"));
  }
  
  private ObservableList<Monster> getMonsters() {
    ObservableList<Monster> monsters = FXCollections.observableArrayList();
    for (int i = 0; i < 100; i++) {
      monsters.add(new Monster("Monster " + i, String.format("Description %d lorem ipsum", i)));
    }
    
    return monsters;
  }
}
