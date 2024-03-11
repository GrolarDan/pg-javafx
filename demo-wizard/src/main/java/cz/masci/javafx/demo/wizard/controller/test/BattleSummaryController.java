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

package cz.masci.javafx.demo.wizard.controller.test;

import cz.masci.javafx.demo.wizard.controller.LeafStep;
import cz.masci.javafx.demo.wizard.controller.WizardStepProvider;
import cz.masci.javafx.demo.wizard.view.BattleStepViewBuilder;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class BattleSummaryController implements WizardStepProvider {

  private final LeafStep wizardStep;

  public BattleSummaryController() {
    wizardStep = LeafStep.builder()
                         .name("Battle Summary")
                         .view(new BattleStepViewBuilder("Battle Summary").build())
                         .updateTitle(this::updateTitle)
                         .build();
  }

  private void updateTitle(StringProperty title) {
    title.set("Battle Summary");
  }
}
