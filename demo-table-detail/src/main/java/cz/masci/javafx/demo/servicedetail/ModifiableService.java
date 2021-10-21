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

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author Daniel
 */
public class ModifiableService {

  private final ObservableMap<Class<?>, ObservableList<Modifiable>> modifiedMap;

  private static final ModifiableService INSTANCE = new ModifiableService();
  
  public static ModifiableService getInstance() {
    return INSTANCE;
  }
          
  private ModifiableService() {
    modifiedMap = FXCollections.observableHashMap();
  }

  public <T extends Modifiable> void add(T item) {
    if (item == null) {
      return;
    }
    
    var modifiedList = getModifiedList(item.getClass());
    // add new item
    modifiedList.add(item);
  }

  public <T extends Modifiable> void remove(T item) {
    if (item == null) {
      return;
    }
    
    var modifiedList = getModifiedList(item.getClass());
    // remove item
    modifiedList.remove(item);
  }
  
  public <T extends Modifiable> boolean contains(T item) {
    if (item == null) {
      return false;
    }
    
    var modifiedList = getModifiedList(item.getClass());
    
    return modifiedList.contains(item);
  }
  
  public <T extends Modifiable> void addListener(Class<T> clazz, ListChangeListener<T> changeListener) {
    ObservableList modifiedList = getModifiedList(clazz);
    
    modifiedList.addListener(changeListener);
    
  }
  
  public <T extends Modifiable> void removeListener(Class<T> clazz, ListChangeListener<T> changeListener) {
    ObservableList modifiedList = getModifiedList(clazz);
    
    modifiedList.removeListener(changeListener);
    
  }
  
  private <T extends Modifiable> ObservableList<Modifiable> getModifiedList(Class<T> clazz) {
    var modifiedList = modifiedMap.get(clazz);

    // check list existence
    if (modifiedList == null) {
      modifiedList = FXCollections.observableArrayList();
      modifiedMap.put(clazz, modifiedList);
    }

    return modifiedList;
  }
}
