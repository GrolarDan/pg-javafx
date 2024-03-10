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

import cz.masci.javafx.demo.wizard.controller.CompositeStep;
import cz.masci.javafx.demo.wizard.controller.WizardStepBuilder;
import cz.masci.javafx.demo.wizard.controller.WizardStepProvider;
import java.util.Arrays;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class BattlePreparationController implements WizardStepProvider {

  private final CompositeStep wizardStep;

  public BattlePreparationController() {
    IntegerProperty groupCount = new SimpleIntegerProperty(0);
    wizardStep = WizardStepBuilder.builder()
                                  .children(Arrays.asList(
                                      new BattlePreparationGroupController(groupCount).getWizardStep(),
                                      new BattlePreparationDuellistController(groupCount).getWizardStep())
                                  )
                                  .updateTitle(this::updateTitle)
                                  .buildCompositeStep();
  }

  public void updateTitle(StringProperty title) {
    if (wizardStep.current() != null) {
      title.set("Preparation - " + title.get());
    }
  }
}
