/*
 * Copyright (c) 2024
 *
 * This file is part of DrD.
 *
 * DrD is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * DrD is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.javafx.demo.wizard.controller.second.steps;

import cz.masci.javafx.demo.wizard.controller.second.LeafStep;
import cz.masci.javafx.demo.wizard.view.BattleStepViewBuilder;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import lombok.Getter;

@Getter
public class BattleDuellistSummaryController implements LeafStep {

  private final Region view;

  public BattleDuellistSummaryController() {
    this.view = new BattleStepViewBuilder("Duellist Summary").build();
  }

  @Override
  public Region view() {
    return view;
  }

  @Override
  public String title() {
    return "Duellist Summary";
  }

  @Override
  public BooleanExpression isValid() {
    return new SimpleBooleanProperty(true);
  }
}
