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
package cz.masci.javafx.demo.service.impl;

import cz.masci.javafx.demo.service.Modifiable;
import cz.masci.javafx.demo.service.ModifiableService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.springframework.stereotype.Service;

/**
 *
 * @author Daniel
 */
@Service
public class ModifiableServiceImpl implements ModifiableService {

  private final ObservableMap<String, ObservableList<Modifiable>> modifiedMap = FXCollections.observableHashMap();

  @Override
  public <T extends Modifiable> void add(T item) {
    if (item == null) {
      return;
    }
    
    add(item.getClass().getSimpleName(), item);
  }

  @Override
  public <T extends Modifiable> void add(String key, T item) {
    var modifiedList = getModifiedList(key);
    // add new item
    modifiedList.add(item);    
  }
  
  @Override
  public <T extends Modifiable> void remove(T item) {
    if (item == null) {
      return;
    }
    
    remove(item.getClass().getSimpleName(), item);
  }
  
  @Override
  public <T extends Modifiable> void remove(String key, T item) {
    var modifiedList = getModifiedList(key);
    // remove item
    modifiedList.remove(item);
  }
  
  @Override
  public <T extends Modifiable> boolean contains(T item) {
    if (item == null) {
      return false;
    }
    
    return contains(item.getClass().getSimpleName(), item);
  }
  
  @Override
  public <T extends Modifiable> boolean contains(String key, T item) {
    var modifiedList = getModifiedList(key);
    
    return modifiedList.contains(item);
  }
  
  @Override
  public <T extends Modifiable> void addListener(Class<T> clazz, ListChangeListener<T> changeListener) {
    addListener(clazz.getSimpleName(), changeListener);
  }
  
  @Override
  public <T extends Modifiable> void addListener(String key, ListChangeListener<T> changeListener) {
    ObservableList modifiedList = getModifiedList(key);
    
    modifiedList.addListener(changeListener);
    
  }
  
  @Override
  public <T extends Modifiable> void removeListener(Class<T> clazz, ListChangeListener<T> changeListener) {
    removeListener(clazz.getSimpleName(), changeListener);
  }
  
  @Override
  public <T extends Modifiable> void removeListener(String key, ListChangeListener<T> changeListener) {
    ObservableList modifiedList = getModifiedList(key);
    
    modifiedList.removeListener(changeListener);
    
  }
  
  private <T extends Modifiable> ObservableList<Modifiable> getModifiedList(String key) {
    var modifiedList = modifiedMap.get(key);

    // check list existence
    if (modifiedList == null) {
      modifiedList = FXCollections.observableArrayList();
      modifiedMap.put(key, modifiedList);
    }

    return modifiedList;
  }
}
