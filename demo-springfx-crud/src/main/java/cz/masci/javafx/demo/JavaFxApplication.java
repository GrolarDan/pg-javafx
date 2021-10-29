/*
 * Copyright (C) 2021 Daniel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.masci.javafx.demo;

import cz.masci.javafx.demo.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Daniel
 */
@Slf4j
public class JavaFxApplication extends Application {

  private ConfigurableApplicationContext applicationContext;

  @Override
  public void init() {
    log.info("INIT - JavaFxApplication");
    String[] args = getParameters().getRaw().toArray(new String[0]);

    this.applicationContext = new SpringApplicationBuilder()
            .sources(SpringApplication.class)
            .run(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    log.info("START - JavaFxApplication");
    FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
    Parent root = fxWeaver.loadView(MainController.class);
    Scene scene = new Scene(root);
    scene.getStylesheets().add("table-detail.css");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    log.info("STOP - JavaFxApplication");
    this.applicationContext.close();
    Platform.exit();
  }

}
