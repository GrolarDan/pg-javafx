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

import cz.masci.javafx.demo.service.Modifiable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;

/**
 * This is abstract controller for Master View editor with list of items.
 * 
 * @author Daniel
 * 
 * @param <T>
 */
@Slf4j
@FxmlView("master-view.fxml")
public abstract class MasterViewController<T extends Modifiable> {

  @FXML
  protected TableView<T> tableView;

  @FXML
  protected VBox items;

  @FXML
  protected Label tableTitle;
  
  @FXML
  protected Label viewTitle;
  
  public final void initialize() {
    init();
  }

  protected abstract void init();
}
