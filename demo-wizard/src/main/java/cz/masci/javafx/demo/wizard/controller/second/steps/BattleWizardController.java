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

import cz.masci.javafx.demo.wizard.controller.second.SimpleCompositeStep;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BattleWizardController extends SimpleCompositeStep {

  public BattleWizardController() {
    addStep(new BattlePreparationController());
    addStep(new BattleDuellistSummaryController());
    addStep(new BattleController());
    addStep(new BattleSummaryController());
  }

  @Override
  protected String getPrevText() {
    return switch (getCurrentIdx()) {
      case 1 -> "Bojovníci";
      case 2 -> "Zrušit kolo";
      case 3 -> "Bitva";
      default -> null;
    };
  }

  @Override
  protected String getNextText() {
    return switch (getCurrentIdx()) {
      case 0 -> "Přehled bojovníků";
      case 1 -> "Spustit bitvu";
      case 2 -> "Přehled bitvy";
      default -> null;
    };
  }
}
