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

import cz.masci.javafx.demo.wizard.model.WizardViewModel;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import io.github.palexdev.materialfx.builders.layout.BorderPaneBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.Optional;
import java.util.function.Supplier;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WizardViewBuilder {

  private final Supplier<Optional<Region>> onPrevAction;
  private final Supplier<Optional<Region>> onNextAction;
  private final WizardViewModel viewModel;

  public Region build(Region firstView) {
    var title = new Text();
    title.setStyle("-fx-fill: WHITE");
    title.textProperty().bind(viewModel.titleProperty());
    var top = new HBox();
    top.setAlignment(Pos.CENTER);
    top.setStyle("-fx-background-color: BLUE");
    top.getChildren().add(title);
    HBox.setHgrow(top, Priority.ALWAYS);

    var navigationBox = new VBox();
    navigationBox.setStyle("-fx-background-color: GREEN; -fx-text-fill: WHITE");
    navigationBox.getChildren().add(new Text("NAVIGATION"));

    var main = new AnchorPane();
    main.setStyle("-fx-background-color: YELLOW");
    setMainView(main, firstView);

    var prevBtn = ButtonBuilder.builder().text("PREVIOUS").styleClass("outlined").command(() -> onAction(main, onPrevAction)).disableExpression(viewModel.prevDisableProperty()).build(MFXButton::new);
    prevBtn.textProperty().bind(viewModel.prevTextProperty());
    AnchorPane.setLeftAnchor(prevBtn, 10.0);
    AnchorPane.setBottomAnchor(prevBtn, 5.0);
    AnchorPane.setTopAnchor(prevBtn, 5.0);
    var nextBtn = ButtonBuilder.builder().text("NEXT").styleClass("filled").command(() -> onAction(main, onNextAction)).disableExpression(viewModel.nextDisableProperty()).build(MFXButton::new);
    nextBtn.textProperty().bind(viewModel.nextTextProperty());
    AnchorPane.setRightAnchor(nextBtn, 10.0);
    AnchorPane.setBottomAnchor(nextBtn, 5.0);
    AnchorPane.setTopAnchor(nextBtn, 5.0);
    var bottom = new AnchorPane();
    bottom.setStyle("-fx-background-color: LIME");
    bottom.getChildren().addAll(prevBtn, nextBtn);

    return new BorderPaneBuilder().setTop(top)
                                  .setLeft(navigationBox)
                                  .setCenter(main)
                                  .setBottom(bottom)
                                  .getNode();
  }

  private void onAction(AnchorPane mainView, Supplier<Optional<Region>> regionSupplier) {
    regionSupplier.get().ifPresent(newView -> {
      setMainView(mainView, newView);
    });
  }

  private void setMainView(AnchorPane mainView, Region view) {
    AnchorPane.setTopAnchor(view, 0.0);
    AnchorPane.setRightAnchor(view, 0.0);
    AnchorPane.setBottomAnchor(view, 0.0);
    AnchorPane.setLeftAnchor(view, 0.0);
    mainView.getChildren().clear();
    mainView.getChildren().add(view);
  }
}
