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

package cz.masci.javafx.demo.wizard.view;

import io.github.palexdev.materialfx.builders.control.TextFieldBuilder;
import io.github.palexdev.materialfx.builders.layout.VBoxBuilder;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import javafx.util.converter.NumberStringConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BattlePreparationGroupStepViewBuilder implements Builder<Region> {

  private final IntegerProperty groupCount;

  @Override
  public Region build() {
    MFXTextField groupNumber = createTextField("Počet skupin", Double.MAX_VALUE);
    Bindings.bindBidirectional(groupNumber.textProperty(), groupCount, new NumberStringConverter());
    return VBoxBuilder.vBox()
                           .setAlignment(Pos.CENTER)
                           .addChildren(groupNumber)
                           .getNode();
  }

  private MFXTextField createTextField(String floatingText, Double maxWidth) {
    return TextFieldBuilder.textField()
                           .setFloatMode(FloatMode.BORDER)
                           .setFloatingText(floatingText)
                           .setMaxWidth(maxWidth)
                           .getNode();
  }

}
