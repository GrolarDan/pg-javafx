/*
 * Copyright (C) 2021 Daniel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.masci.javafx.demo.servicedetail;

import cz.masci.javafx.demo.dto.MonsterDTO;
import cz.masci.javafx.demo.service.MonsterService;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author Daniel
 */
public class PrimaryController {

  private MonsterService monsterService;
  private StyleChangingRowFactory<MonsterDTO> rowFactory;

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
  private cz.masci.javafx.demo.servicedetail.DetailController detailsController;

  public PrimaryController() {
    monsterService = new MonsterService();
    rowFactory = new StyleChangingRowFactory<>("edited-row", MonsterDTO.class);
  }

  @FXML
  public void initialize() {
    tableView.setItems(monsterService.getMonsters());
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    description.setCellValueFactory(new PropertyValueFactory<>("description"));

    tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      detailsController.setMonster(newValue);
    });

    tableView.setRowFactory(rowFactory);
  }

  public class StyleChangingRowFactory<T extends Modifiable> implements Callback<TableView<T>, TableRow<T>> {

    private final ModifiableService modifiableService;
    private final String styleClass;
    private final Class<T> itemClass;
    private final Callback<TableView<T>, TableRow<T>> baseFactory;

    public StyleChangingRowFactory(String styleClass, Class<T> clazz, Callback<TableView<T>, TableRow<T>> baseFactory) {
      this.styleClass = styleClass;
      this.itemClass = clazz;
      this.baseFactory = baseFactory;
      this.modifiableService = ModifiableService.getInstance();
    }

    public StyleChangingRowFactory(String styleClass, Class<T> clazz) {
      this(styleClass, clazz, null);
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

      modifiableService.addListener(itemClass, (change) -> updateStyleClass(row));

      return row;
    }

    private void updateStyleClass(TableRow<T> row) {
      final ObservableList<String> rowStyleClasses = row.getStyleClass();
      if (modifiableService.contains(row.getItem())) {
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
