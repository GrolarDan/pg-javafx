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
import cz.masci.javafx.demo.service.ModifiableService;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Daniel
 * 
 * @param <T>
 */
@Slf4j
@RequiredArgsConstructor
public abstract class DetailViewController<T extends Modifiable> {

  private final ModifiableService modifiableService;

  private List<ObservableValue<String>> observableValues;
  private ChangeListener<String> listener;
  private T item;

  public void setItem(T item) {
    if (this.item != null) {
      unhookListener();
    }
    this.item = item;
    hookTo(item);
  }

  public T getItem() {
    return item;
  }

  private void unhookListener() {
    getObservableValues().forEach(t -> t.removeListener(listener));
  }

  private void hookTo(T item) {
    fillInputs(item);
    if (item == null) {
      listener = null;
    } else {
      listener = (observable, oldValue, newValue) -> {
        changed(observable, oldValue, newValue);
        modifiableService.add(item);
      };
      getObservableValues().forEach(t -> t.addListener(listener));
    }
  }

  private List<ObservableValue<String>> getObservableValues() {
    if (observableValues == null) {
      observableValues = List.copyOf(initObservableValues());
    }
    return observableValues;
  }

  protected abstract List<ObservableValue<String>> initObservableValues();

  protected abstract void fillInputs(T item);

  protected abstract void changed​(ObservableValue<? extends String> observable, String oldValue, String newValue);

}
