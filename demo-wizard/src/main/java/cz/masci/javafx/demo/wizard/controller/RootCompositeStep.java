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

package cz.masci.javafx.demo.wizard.controller;

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class RootCompositeStep extends CompositeStep {

  public RootCompositeStep() {
    setUpdateNextDisable(this::updateNextDisable);
    setUpdatePrevDisable(this::updatePrevDisable);
  }

  @Override
  public void next(WizardViewModel wizardViewModel) {
    super.next(wizardViewModel);
    updateNextDisable(wizardViewModel.nextDisableProperty());
    updatePrevDisable(wizardViewModel.prevDisableProperty());
  }

  @Override
  public void previous(WizardViewModel wizardViewModel) {
    super.previous(wizardViewModel);
    updateNextDisable(wizardViewModel.nextDisableProperty());
    updatePrevDisable(wizardViewModel.prevDisableProperty());
  }

  private void updateNextDisable(BooleanProperty disable) {
    if (currentStep != null) {
      var noNextStep = !hasNext() && !currentStep.hasNext();
      if (disable.isBound()) {
        disable.or(new SimpleBooleanProperty(noNextStep));
      } else {
        disable.set(noNextStep);
      }
    } else {
      if (disable.isBound()) {
        disable.unbind();
      }
      disable.set(!hasNext());
    }
  }

  private void updatePrevDisable(BooleanProperty disable) {
    if (currentStep != null) {
      var noPrevStep = !hasPrevious() && !currentStep.hasPrevious();
      if (disable.isBound()) {
        disable.or(new SimpleBooleanProperty(noPrevStep));
      } else {
        disable.set(noPrevStep);
      }
    } else {
      if (disable.isBound()) {
        disable.unbind();
      }
      disable.set(!hasPrevious());
    }
  }
}
