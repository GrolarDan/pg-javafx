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
import cz.masci.javafx.demo.wizard.view.BattlePreparationGroupStepViewBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.IntegerProperty;
import javafx.scene.layout.Region;
import lombok.Getter;

@Getter
public class BattlePreparationGroupController implements LeafStep {

  private final Region view;
  private final IntegerProperty groupCount;

  public BattlePreparationGroupController(IntegerProperty groupCount) {
    this.view = new BattlePreparationGroupStepViewBuilder(groupCount).build();
    this.groupCount = groupCount;
  }

  @Override
  public Region view() {
    return view;
  }

  @Override
  public String title() {
    return "Skupiny";
  }

  @Override
  public BooleanExpression isValid() {
    return Bindings.or(groupCount.asObject().isNull(), groupCount.lessThan(2));
  }
}
