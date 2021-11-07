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
package cz.masci.javafx.demo.controller;

import cz.masci.javafx.demo.service.EditControllerService;
import cz.masci.javafx.demo.service.Modifiable;
import cz.masci.javafx.demo.service.ModifiableService;
import cz.masci.javafx.demo.utility.StyleChangingRowFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import cz.masci.javafx.demo.service.CrudService;
import java.util.List;
import javafx.collections.FXCollections;

/**
 * This is abstract controller for Master View editor with list of items.
 *
 * @author Daniel
 *
 * @param <T>
 */
@Slf4j
@RequiredArgsConstructor
@FxmlView("master-view.fxml")
public abstract class MasterViewController<T extends Modifiable> {

  private final FxWeaver fxWeaver;
  private final CrudService<T> itemService;
  private final String itemKey;
  private final Class<? extends EditControllerService<T>> editControllerClass;
  private ModifiableService modifiableService;

  @FXML
  protected BorderPane borderPane;

  @FXML
  protected TableView<T> tableView;

  @FXML
  protected VBox items;

  @FXML
  protected Label tableTitle;

  @FXML
  protected Label viewTitle;

  @FXML
  public void onNewItem(ActionEvent event) {
    FxControllerAndView<? extends EditControllerService<T>, DialogPane> editor = fxWeaver.load(editControllerClass);
    Dialog<T> dialog = new Dialog<>();
    dialog.setTitle("New Item");
    dialog.setDialogPane(editor.getView().get());
    dialog.setResultConverter(editor.getController().getResultConverter());
    dialog.showAndWait()
            .ifPresent(item -> {
              itemService.create(item);
              tableView.getItems().add(item);
              tableView.getSelectionModel().select(item);
              tableView.scrollTo(item);
            });
  }

  @FXML
  public void onSaveAll(ActionEvent event) {
    Alert alert = new Alert(AlertType.INFORMATION, "Saving all items");
    alert.showAndWait().ifPresent(button -> {
      List<T> modifiedList = modifiableService.getAll(itemKey);
      modifiedList.stream()
              .forEach(item -> {
                itemService.update(item);
                modifiableService.remove(item);
              });
    });
  }

  @FXML
  public void onDelete(ActionEvent event) {
    Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to delete selected item?");
    alert.showAndWait().ifPresent(button -> {
      T item = tableView.getSelectionModel().getSelectedItem();
      tableView.getItems().remove(item);
      itemService.delete(item);
      modifiableService.remove(item);
    });
  }

  @Autowired
  public final void setModifiableService(ModifiableService modifiableService) {
    this.modifiableService = modifiableService;
  }

  public final void initialize() {
    init();
    var newList = FXCollections.<T>observableArrayList();
    newList.addAll(itemService.list());
    tableView.setItems(newList);
  }

  protected void setViewTitle(String title) {
    viewTitle.setText(title);
  }

  protected void setTableTitle(String title) {
    tableTitle.setText(title);
  }

  public void clearCollumns() {
    tableView.getColumns().clear();
  }

  public void addCollumns(TableColumn<T, ?>... collumns) {
    tableView.getColumns().addAll(collumns);
  }

  public <E extends DetailViewController<T>> void setDetailController(Class<E> detailController) {
    setDetailController(detailController, detailController.getSimpleName());
  }

  public <E extends DetailViewController<T>> void setDetailController(Class<E> detailController, String modifiableKey) {
    FxControllerAndView<E, Node> detailView = fxWeaver.load(detailController);

    borderPane.setCenter(detailView.getView().get());

    tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      detailView.getController().setItem(newValue);
    });

  }

  protected void setRowFactory(String styleClass, Class<T> clazz) {
    setRowFactory(styleClass, clazz.getSimpleName());
  }

  protected void setRowFactory(String styleClass, String modifiableKey) {
    tableView.setRowFactory(new StyleChangingRowFactory<>(styleClass, modifiableKey, modifiableService));
  }

  /**
   * Delegate titles and collumns initialization to subclass.
   */
  protected abstract void init();

}
