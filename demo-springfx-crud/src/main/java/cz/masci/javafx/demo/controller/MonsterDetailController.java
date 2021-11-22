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

import cz.masci.javafx.demo.FxmlController;
import cz.masci.javafx.demo.control.MonsterDetailControl;
import cz.masci.javafx.demo.controller.base.DetailViewController;
import cz.masci.javafx.demo.dto.MonsterDTO;
import cz.masci.javafx.demo.service.ModifiableService;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

/**
 *
 * @author Daniel
 */
@Component
@Slf4j
@FxmlView("monster-detail.fxml")
@FxmlController
public class MonsterDetailController extends DetailViewController<MonsterDTO> {

  @FXML
  private MonsterDetailControl detail;

  public MonsterDetailController(ModifiableService modifiableService) {
    super(MonsterDTO.class.getSimpleName(), modifiableService);
  }
  
  @Override
  protected List<ObservableValue<String>> initObservableValues() {
    log.info("Init observable values");

    return List.of(
            detail.descriptionProperty()
    );
  }

  @Override
  protected void fillInputs(MonsterDTO item) {
    log.info("Filling inputs {}", item);

    if (item == null) {
      detail.setName("");
      detail.setDescription("");
    } else {
      detail.setName(item.getName());
      detail.setDescription(item.getDescription());
    }
  }

  @Override
  protected void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    log.info("changes - newValue: {}", newValue);
    log.info("changes - observable: {}", observable);
    
    if (detail.descriptionProperty().equals(observable)) {
      getItem().setDescription(newValue);
    }
  }

}
