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
import java.util.Arrays;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BattleWizardController implements WizardStepProvider {

  private final CompositeStep wizardStep;

  public BattleWizardController() {
    wizardStep = CompositeStep.builder()
                              .children(Arrays.asList(new BattlePreparationController().getWizardStep(),
                                  new BattleDuellistSummaryController().getWizardStep(), new BattleController().getWizardStep(),
                                  new BattleSummaryController().getWizardStep()))
                              .updateNextDisable(this::updateNextDisable)
                              .updatePrevDisable(this::updatePrevDisable)
                              .build();
  }

  private void updateNextDisable(BooleanProperty disable) {
    if (wizardStep.current() != null) {
      var step = wizardStep.current();
      var noNextStep = wizardStep.hasNoNext() && step.hasNoNext();
      if (disable.isBound()) {
        disable.or(new SimpleBooleanProperty(noNextStep));
      } else {
        disable.set(noNextStep);
      }
    } else {
      if (disable.isBound()) {
        disable.unbind();
      }
      disable.set(wizardStep.hasNoNext());
    }
  }

  private void updatePrevDisable(BooleanProperty disable) {
    if (wizardStep.current() != null) {
      var step = wizardStep.current();
      var noPrevStep = wizardStep.hasNoPrevious() && step.hasNoPrevious();
      if (disable.isBound()) {
        disable.or(new SimpleBooleanProperty(noPrevStep));
      } else {
        disable.set(noPrevStep);
      }
    } else {
      if (disable.isBound()) {
        disable.unbind();
      }
      disable.set(wizardStep.hasNoPrevious());
    }
  }
}
