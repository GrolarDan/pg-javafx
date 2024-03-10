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
import cz.masci.javafx.demo.wizard.controller.WizardStepProvider;
import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.property.IntegerProperty;
import lombok.Getter;

@Getter
public class BattlePreparationDuellistController implements WizardStepProvider {

  private final CompositeStep wizardStep;

  public BattlePreparationDuellistController(IntegerProperty groupCount) {
    wizardStep = CompositeStep.builder()
                              .beforeFirstNext(initChildren(groupCount))
                              .build();
  }

  private Consumer<WizardViewModel> initChildren(IntegerProperty groupCount) {
    return wizardViewModel -> {
      var groups = IntStream.range(0, groupCount.get())
                            .mapToObj(i -> String.format("Skupina %d", i + 1))
                            .map(BattlePreparationDuellistChildController::new)
                            .map(WizardStepProvider::getWizardStep)
                            .collect(Collectors.toList());
      wizardStep.setChildren(groups);
    };
  }
}
