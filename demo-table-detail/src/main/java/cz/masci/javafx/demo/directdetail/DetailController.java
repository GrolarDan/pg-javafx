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
package cz.masci.javafx.demo.directdetail;

import cz.masci.javafx.demo.dto.MonsterDTO;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author Daniel
 */
public class DetailController {

  private ChangeListener<String> listener;
  private MonsterDTO monster;

  @FXML
  private Label name;

  @FXML
  private TextArea description;

  public void setMonster(MonsterDTO monster) {
    if (this.monster != null) {
      unhookListener();
    }
    this.monster = monster;
    hookTo(monster);
  }

  private void unhookListener() {
    description.textProperty().removeListener(listener);
  }

  private void hookTo(MonsterDTO monster) {
    if (monster == null) {
      name.setText("");
      description.setText("");
      listener = null;
    } else {
      name.setText(monster.getName());
      description.setText(monster.getDescription());
      listener = (observable, oldValue, newValue) -> {
        monster.setDescription(newValue);
        monster.setEdited(true);
      };
      description.textProperty().addListener(listener);
    }
  }

}
