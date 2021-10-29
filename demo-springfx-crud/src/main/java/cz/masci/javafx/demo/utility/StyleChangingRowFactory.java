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
package cz.masci.javafx.demo.utility;

import cz.masci.javafx.demo.service.Modifiable;
import cz.masci.javafx.demo.service.ModifiableService;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Daniel
 */
  public class StyleChangingRowFactory<T extends Modifiable> implements Callback<TableView<T>, TableRow<T>> {

    private final ModifiableService modifiableService;
    private final String styleClass;
    private final String modifiableKey;
    private final Callback<TableView<T>, TableRow<T>> baseFactory;

    public StyleChangingRowFactory(String styleClass, String modifiableKey, ModifiableService modifiableService, Callback<TableView<T>, TableRow<T>> baseFactory) {
      this.styleClass = styleClass;
      this.modifiableKey = modifiableKey;
      this.modifiableService = modifiableService;
      this.baseFactory = baseFactory;
    }

    public StyleChangingRowFactory(String styleClass, String modifiableKey, ModifiableService modifiableService) {
      this(styleClass, modifiableKey, modifiableService, null);
    }

    public StyleChangingRowFactory(String styleClass, Class<T> modifiableKey, ModifiableService modifiableService, Callback<TableView<T>, TableRow<T>> baseFactory) {
      this(styleClass, modifiableKey.getSimpleName(), modifiableService, baseFactory);
    }

    public StyleChangingRowFactory(String styleClass, Class<T> modifiableKey, ModifiableService modifiableService) {
      this(styleClass, modifiableKey, modifiableService, null);
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

      modifiableService.addListener(modifiableKey, (change) -> updateStyleClass(row));

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
