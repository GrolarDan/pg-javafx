package cz.masci.javafx.demo.abstractdetail;

import cz.masci.javafx.demo.service.MonsterService;
import cz.masci.javafx.demo.dto.MonsterDTO;
import java.util.Collections;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimaryController {

  private MonsterService monsterService;
  private StyleChangingRowFactory<MonsterDTO> rowFactory;
  private MonsterDTO monster;

  @FXML
  private TableView<MonsterDTO> tableView;

  @FXML
  private TableColumn<MonsterDTO, String> name;

  @FXML
  private TableColumn<MonsterDTO, String> description;

  @FXML
  private VBox details;

  // Name of this class has to be prefixed by the name of VBox variable. Means root node of the included fxml
  @FXML
  private DetailController detailsController;

  public PrimaryController() {
    log("constructor");
    monsterService = new MonsterService();
    rowFactory = new StyleChangingRowFactory<>("edited-row");
  }

  @FXML
  public void initialize() {
    log("initialize");
    tableView.setItems(monsterService.getMonsters());
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    description.setCellValueFactory(new PropertyValueFactory<>("description"));
    // listen to modified property change
    detailsController.modifiedProperty().addListener((observableBool, oldValueBool, newValueBool) -> {
      log("Edited property changed for item: " + monster + " - " + newValueBool);
      if (Boolean.TRUE.equals(newValueBool)) {
        rowFactory.getStyledItems().add(monster);
      } else {
        rowFactory.getStyledItems().remove(monster);
      }
    });

    tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      log("Selected item: " + newValue);
      monster = newValue;
      detailsController.setItem(monster);
    });

    tableView.setRowFactory(rowFactory);
  }

  private void log(String text) {
    log.info("{} : {}", this.getClass().getCanonicalName(), text);    
  }
  
  public class StyleChangingRowFactory<T> implements Callback<TableView<T>, TableRow<T>> {

    private final String styleClass;
    private final ObservableList<MonsterDTO> styledItems;
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

      styledItems.addListener(new ListChangeListener<MonsterDTO>() {
        @Override
        public void onChanged(Change<? extends MonsterDTO> change) {
          updateStyleClass(row);
        }

      });

      return row;
    }

    public ObservableList<MonsterDTO> getStyledItems() {
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
