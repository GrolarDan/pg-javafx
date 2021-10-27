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
package cz.masci.javafx.demo.service;

import javafx.collections.ListChangeListener;

/**
 *
 * @author Daniel
 */
public interface ModifiableService {

  <T extends Modifiable> void add(T item);

  <T extends Modifiable> void add(String key, T item);

  <T extends Modifiable> void addListener(Class<T> clazz, ListChangeListener<T> changeListener);

  <T extends Modifiable> void addListener(String key, ListChangeListener<T> changeListener);

  <T extends Modifiable> boolean contains(T item);

  <T extends Modifiable> boolean contains(String key, T item);

  <T extends Modifiable> void remove(T item);

  <T extends Modifiable> void remove(String key, T item);

  <T extends Modifiable> void removeListener(Class<T> clazz, ListChangeListener<T> changeListener);

  <T extends Modifiable> void removeListener(String key, ListChangeListener<T> changeListener);
  
}
