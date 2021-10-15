package cz.masci.javafx.demo;

import java.util.Collections;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MainController {

  private MonsterService monsterService;
  private StyleChangingRowFactory<Monster> rowFactory;
  private ChangeListener<Boolean> listener;
  private Monster monster;

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

  public MainController() {
    monsterService = new MonsterService();
    rowFactory = new StyleChangingRowFactory<>("edited-row");
  }

  @FXML
  public void initialize() {
    tableView.setItems(monsterService.getMonsters());
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    description.setCellValueFactory(new PropertyValueFactory<>("description"));

    tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (monster != null) {
        monster.editedProperty().removeListener(listener);
      }
      
      if (newValue != null) {
        monster = newValue;
        listener = (observableBool, oldValueBool, newValueBool) -> {
          System.out.println("Edited property changed: " + newValueBool);
          if (Boolean.TRUE.equals(newValueBool)) {
            rowFactory.getStyledItems().add(monster);
          } else {
            rowFactory.getStyledItems().remove(monster);
          }
        };
        monster.editedProperty().addListener(listener);
      }
      
      detailsController.setMonster(monster);
    });

    tableView.setRowFactory(rowFactory);
  }

  public class StyleChangingRowFactory<T> implements Callback<TableView<T>, TableRow<T>> {

    private final String styleClass;
    private final ObservableList<Monster> styledItems;
    private final Callback<TableView<T>, TableRow<T>> baseFactory;

    public StyleChangingRowFactory(String styleClass, Callback<TableView<T>, TableRow<T>> baseFactory) {
      this.styleClass = styleClass;
      this.baseFactory = baseFactory;
      this.styledItems = FXCollections.observableArrayList();
    }

    public StyleChangingRowFactory(String styleClass) {
      this(styleClass, null);
    }

    @Override
    public TableRow<T> call(TableView<T> tableView) {

      final TableRow<T> row;
      if (baseFactory == null) {
        row = new TableRow<>();
      } else {
        row = baseFactory.call(tableView);
      }

      row.itemProperty().addListener((obs, oldValue, newValue) -> updateStyleClass(row));

      styledItems.addListener(new ListChangeListener<Monster>() {
        @Override
        public void onChanged(Change<? extends Monster> change) {
          updateStyleClass(row);
        }

      });

      return row;
    }

    public ObservableList<Monster> getStyledItems() {
      return styledItems;
    }

    private void updateStyleClass(TableRow<T> row) {
      final ObservableList<String> rowStyleClasses = row.getStyleClass();
      if (styledItems.contains(row.getItem())) {
        if (!rowStyleClasses.contains(styleClass)) {
          rowStyleClasses.add(styleClass);
        }
      } else {
        // remove all occurrences of styleClass:
        rowStyleClasses.removeAll(Collections.singleton(styleClass));
      }
    }

  }
}
