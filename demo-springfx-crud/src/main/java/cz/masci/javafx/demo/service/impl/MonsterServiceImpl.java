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

import cz.masci.javafx.demo.dto.MonsterDTO;
import cz.masci.javafx.demo.service.ItemService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Daniel
 */
@Service
@Slf4j
public class MonsterServiceImpl implements ItemService<MonsterDTO> {

  @Override
  public ObservableList<MonsterDTO> getItems() {
    log.info("Loading Monsters ...");
    ObservableList<MonsterDTO> monsters = FXCollections.observableArrayList();
    for (int i = 0; i < 100; i++) {
      monsters.add(new MonsterDTO("Monster " + i, String.format("Description %d lorem ipsum", i)));
    }

    return monsters;
  }

  @Override
  public MonsterDTO addItem(MonsterDTO item) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
