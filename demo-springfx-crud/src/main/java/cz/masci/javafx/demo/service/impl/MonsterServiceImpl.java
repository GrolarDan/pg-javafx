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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cz.masci.javafx.demo.service.CrudService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
@Service
@Slf4j
public class MonsterServiceImpl implements CrudService<MonsterDTO> {
  
  private final List<MonsterDTO> monsters;

  public MonsterServiceImpl() {
    monsters = new ArrayList();
    for (int i = 0; i < 100; i++) {
      monsters.add(new MonsterDTO("Monster " + i, String.format("Description %d lorem ipsum", i)));
    }
  }
  

  @Override
  public List<MonsterDTO> list() {
    log.info("Loading Monsters ...");

    return monsters;
  }

  @Override
  public MonsterDTO create(MonsterDTO item) {
    log.info("Adding new monster: {}", item);
    
    monsters.add(item);
    
    return item;
  }

  @Override
  public MonsterDTO delete(MonsterDTO item) {
    log.info("Deleting monster: {}", item);
    
    monsters.remove(item);
    
    return item;
  }

  @Override
  public MonsterDTO update(MonsterDTO item) {
    log.info("Updating monster: {}", item);

    return item;
  }

}
