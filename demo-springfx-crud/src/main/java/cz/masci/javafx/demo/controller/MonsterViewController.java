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

import cz.masci.javafx.demo.dto.MonsterDTO;
import cz.masci.javafx.demo.service.MonsterService;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

/**
 *
 * @author Daniel
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MonsterViewController extends MasterViewController<MonsterDTO> {

  private final FxWeaver fxWeaver;
  private final MonsterService monsterService;

  @FXML
  private TableColumn<MonsterDTO, String> name;

  @FXML
  private TableColumn<MonsterDTO, String> description;

  @Override
  protected void init() {
    log.info("initialize");

    tableTitle.setText("List of Monsters");
    viewTitle.setText("Monsters");

    name = new TableColumn<>("Name");
    name.setPrefWidth(100.0);
    name.setCellValueFactory(new PropertyValueFactory<>("name"));

    description = new TableColumn<>("Description");
    description.setPrefWidth(200.0);
    description.setCellValueFactory(new PropertyValueFactory<>("description"));

    tableView.getColumns().addAll(List.of(name, description));

    tableView.setItems(monsterService.getMonsters());

    FxControllerAndView<MonsterDetailController, VBox> detailView = fxWeaver.load(MonsterDetailController.class);

    borderPane.setCenter(detailView.getView().get());

    tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      detailView.getController().setItem(newValue);
    });

  }

}
