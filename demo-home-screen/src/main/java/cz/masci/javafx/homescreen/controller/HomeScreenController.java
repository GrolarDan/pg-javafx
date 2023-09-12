package cz.masci.javafx.homescreen.controller;

import static cz.masci.javafx.homescreen.FXResourcesLoader.loadURL;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.LoaderCacheLevel;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeScreenController implements Initializable {

  private final MFXLoader mfxLoader;
  private final Map<String, Stage> stages;

  @FXML
  private MFXButton btn1;

  @FXML
  private MFXButton btn2;

  @FXML
  private MFXButton btn3;

  @FXML
  private MFXButton btn4;

  @FXML
  private MFXButton btn5;

  @FXML
  private MFXButton btn6;

  public HomeScreenController() {
    stages = new HashMap<>(6);
    mfxLoader = new MFXLoader();
    mfxLoader.setCacheLevel(LoaderCacheLevel.NONE);
  }

  @FXML
  void btnAction(ActionEvent event) {
    MFXButton source = (MFXButton) event.getSource();
    var stage = stages.get(source.getId());
    if (stage == null) {
      var bean = mfxLoader.getView(source.getId());
      Scene scene = new Scene(bean.getRoot(), 200, 200);
      stage = new Stage();
      stage.setScene(scene);
      stages.put(source.getId(), stage);
    }
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializeLoader();
  }

  private void initializeLoader() {
    List.of(btn1, btn2, btn3, btn4, btn5, btn6)
        .forEach(btn -> mfxLoader.addView(
            MFXLoaderBean.of(btn.getId(), loadURL("fxml/hello-view.fxml"))
                .setControllerFactory(c -> new HelloController(btn.getText()))
                .get()
        ));
    mfxLoader.start();
  }

}
