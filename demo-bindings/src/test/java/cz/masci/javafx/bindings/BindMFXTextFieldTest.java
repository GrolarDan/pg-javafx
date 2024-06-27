package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactfx.EventStreams;
import org.reactfx.value.Val;
import org.reactfx.value.Var;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

@ExtendWith(ApplicationExtension.class)
public class BindMFXTextFieldTest {

  private MFXTextField name;
  private MFXTextField description;

  @Start
  private void start(Stage stage) {
    name = new MFXTextField();
    description = new MFXTextField();
    FlowPane pane = new FlowPane(name, description);
    stage.setScene(new Scene(pane, 640, 400));
    stage.show();
  }

  @Stop
  private void stop() {
    name.textProperty().unbind();
    description.textProperty().unbind();
  }

  @Test
  void test_null_source(FxRobot robot) {
    // prepare null initial value
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    Var<TestModel> src = Var.newSimpleVar(null);
    Var<String> dstName = selectVarOrElseConst(src, TestModel::nameProperty, "");
    Var<String> dstDescription = selectVarOrElseConst(src, TestModel::descriptionProperty, "");
    name.textProperty().bindBidirectional(dstName);
    description.textProperty().bindBidirectional(dstDescription);

    // assert
    assertEquals("Name", model.getName());
    assertEquals("Description", model.getDescription());
    assertEquals("", name.getText());
    assertEquals("", description.getText());

    // fill text field when src is null
    // act
    robot.doubleClickOn(name);
    robot.write("Super Name");
    // assert
    assertEquals("Name", model.getName());
    assertEquals("Description", model.getDescription());
    assertEquals("Super Name", name.getText());
    assertEquals("", description.getText());

    // set model
    // act
    src.setValue(model);
    // assert
    assertEquals("Name", model.getName());
    assertEquals("Description", model.getDescription());
    assertEquals("Name", name.getText());
    assertEquals("Description", description.getText());

    // fill text field
    // act
    robot.doubleClickOn(name);
    robot.write("New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("Description", model.getDescription());
    assertEquals("New Name", name.getText());
    assertEquals("Description", description.getText());
  }

  private <T, U> Var<U> selectVarOrElseConst(Var<T> src, Function<T, Property<U>> property, U constValue) {
    return src.flatMap(property)
        .orElseConst(constValue)
        .asVar(newValue -> src.ifPresent(srcProperty -> property.apply(srcProperty).setValue(newValue)));
  }
}
