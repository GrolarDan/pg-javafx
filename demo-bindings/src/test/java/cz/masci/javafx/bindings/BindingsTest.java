package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactfx.value.Var;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class BindingsTest {

  // region bind simple
  @Test
  void bind_simple_oneDirectional() {
    // prepare
    StringProperty src = new SimpleStringProperty();
    StringProperty dst = new SimpleStringProperty();
    dst.bind(src);

    // from src -> dst
    // act
    src.set("Hello");
    // assert
    assertEquals("Hello", src.getValue());
    assertEquals("Hello", dst.getValue());

    // set dst only
    // act
    assertThrows(RuntimeException.class, () -> dst.set("Success"));
    // assert
    assertEquals("Hello", src.getValue());
    assertEquals("Hello", dst.getValue());
  }

  @Test
  void bind_simple_biDirectional() {
    // prepare
    StringProperty src = new SimpleStringProperty();
    StringProperty dst = new SimpleStringProperty();
    dst.bindBidirectional(src);

    // from src -> dst
    // act
    src.set("Hello");
    // assert
    assertEquals("Hello", src.getValue());
    assertEquals("Hello", dst.getValue());

    // from dst -> src
    // act
    dst.set("Success");
    // assert
    assertEquals("Success", src.getValue());
    assertEquals("Success", dst.getValue());
  }

  @Test
  void bind_simple_biDirectional_use_bindings() {
    // prepare
    StringProperty src = new SimpleStringProperty();
    StringProperty dst = new SimpleStringProperty();
    Bindings.bindBidirectional(src, dst);

    // update src
    // act
    src.set("Hello");
    // assert
    assertEquals("Hello", src.getValue());
    assertEquals("Hello", dst.getValue());

    // update dst
    // act
    dst.set("Success");
    // assert
    assertEquals("Success", src.getValue());
    assertEquals("Success", dst.getValue());
  }
  // endregion
  // region bind property of property
  @Test
  void bind_property_of_property_oneDirectional() {
    // prepare
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    ObjectProperty<TestModel> src = new SimpleObjectProperty<>();
    StringProperty dstName = new SimpleStringProperty();
    StringProperty dstDescription = new SimpleStringProperty();
    dstName.bind(Bindings.selectString(src, "name"));
    dstDescription.bind(Bindings.selectString(src, "description"));

    // assert
    assertNull(dstName.getValue());
    assertNull(dstDescription.getValue());

    // set src
    // act
    src.setValue(model);
    // assert
    assertNotNull(dstName.getValue());
    assertEquals("Name", dstName.getValue());
    assertNotNull(dstDescription.getValue());
    assertEquals("Description", dstDescription.getValue());

    // update src property
    model.setName("New name");
    // assert
    assertNotNull(dstName.getValue());
    assertEquals("New name", dstName.getValue());
    assertNotNull(dstDescription.getValue());
    assertEquals("Description", dstDescription.getValue());
  }

  @Test
  void bind_property_of_property_biDirectional() {
    // prepare
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    ObjectProperty<TestModel> src = new SimpleObjectProperty<>(model);
    StringProperty dstName = new SimpleStringProperty();
    StringProperty dstDescription = new SimpleStringProperty();
    dstName.bindBidirectional(model.nameProperty());
    dstDescription.bindBidirectional(model.descriptionProperty());

    // assert
    assertEquals("Name", dstName.getValue());
    assertEquals("Description", dstDescription.getValue());

    // update model property
    model.setName("New name");
    // assert
    assertEquals("New name", dstName.getValue());
    assertEquals("New name", src.getValue().getName());
    assertEquals("Description", dstDescription.getValue());
    assertEquals("Description", src.getValue().getDescription());

    // update dst property
    dstDescription.setValue("New Description");
    // assert
    assertEquals("New name", dstName.getValue());
    assertEquals("New name", src.getValue().getName());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", src.getValue().getDescription());

    // set src
    // act
    src.setValue(new TestModel());
    // assert
    assertEquals("New name", dstName.getValue());
    assertNull(src.getValue().getName());
    assertEquals("New Description", dstDescription.getValue());
    assertNull(src.getValue().getDescription());
  }

  @Test
  void bind_property_of_property_biDirectional_nullable() {
    // prepare
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    Var<TestModel> src = Var.newSimpleVar(model);
    Var<String> dstName = src.selectVar(TestModel::nameProperty);
    Var<String> dstDescription = src.selectVar(TestModel::descriptionProperty);

    // assert
    assertEquals("Name", dstName.getValue());
    assertEquals("Description", dstDescription.getValue());

    // update model
    // act
    model.setName("New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("Description", model.getDescription());
    assertEquals("Description", dstDescription.getValue());

    // update dst
    // act
    dstDescription.setValue("New Description");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Description", dstDescription.getValue());

    // set new src
    // act
    TestModel newModel = new TestModel();
    newModel.setName("New Model Name");
    newModel.setDescription("New Model Description");
    src.setValue(newModel);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model Name", newModel.getName());
    assertEquals("New Model Name", dstName.getValue());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());

    // update dst when connected to new model
    // act
    dstName.setValue("New Model New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Model New Name", dstName.getValue());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());

    // set back old model
    // act
    src.setValue(model);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Description", dstDescription.getValue());

    // set null src
    // act
    src.setValue(null);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertNull(dstName.getValue());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertNull(dstDescription.getValue());
  }

  @Test
  void bind_property_of_property_biDirectional_with_TextField() {
    // prepare
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    Var<TestModel> src = Var.newSimpleVar(model);
    Var<String> dstName = src.selectVar(TestModel::nameProperty);
    Var<String> dstDescription = src.selectVar(TestModel::descriptionProperty);
    TextField name = new TextField();
    name.textProperty().bindBidirectional(dstName);
    TextField description = new TextField();
    description.textProperty().bindBidirectional(dstDescription);

    // assert
    assertEquals("Name", dstName.getValue());
    assertEquals("Description", dstDescription.getValue());
    assertEquals("Name", name.getText());
    assertEquals("Description", description.getText());

    // update model
    // act
    model.setName("New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("Description", model.getDescription());
    assertEquals("Description", dstDescription.getValue());
    assertEquals("Description", description.getText());

    // update dst
    // act
    dstDescription.setValue("New Description");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

    // set new src
    // act
    TestModel newModel = new TestModel();
    newModel.setName("New Model Name");
    newModel.setDescription("New Model Description");
    src.setValue(newModel);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model Name", newModel.getName());
    assertEquals("New Model Name", dstName.getValue());
    assertEquals("New Model Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());
    assertEquals("New Model Description", description.getText());

    // update dst when connected to new model
    // act
    dstName.setValue("New Model New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Model New Name", dstName.getValue());
    assertEquals("New Model New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());
    assertEquals("New Model Description", description.getText());

    // set back old model
    // act
    src.setValue(model);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

    // set null src
    // act
    src.setValue(null);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertNull(dstName.getValue());
    assertNull(name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertNull(dstDescription.getValue());
    assertNull(description.getText());

    // set text field value
    // act
    src.setValue(model);
    name.setText("Field Name");
    // assert
    assertEquals("Field Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("Field Name", dstName.getValue());
    assertEquals("Field Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

  }

  @Test
  void bind_property_of_property_biDirectional_with_MFXTextField() {
    // prepare
    TestModel model = new TestModel();
    model.setName("Name");
    model.setDescription("Description");
    Var<TestModel> src = Var.newSimpleVar(null);
    Var<String> dstName = src.selectVar(TestModel::nameProperty);
    Var<String> dstDescription = src.selectVar(TestModel::descriptionProperty);
    MFXTextField name = new MFXTextField();
    name.textProperty().bindBidirectional(dstName);
    MFXTextField description = new MFXTextField();
    description.textProperty().bindBidirectional(dstDescription);

    // assert
    assertNull(dstName.getValue());
    assertNull(dstDescription.getValue());
    assertNull(name.getText());
    assertNull(description.getText());

    // update model when not bound
    // act
    model.setName("Name 2");
    // assert
    assertNull(dstName.getValue());
    assertNull(dstDescription.getValue());
    assertNull(name.getText());
    assertNull(description.getText());

    // set model
    // act
    src.setValue(model);
    // assert
    assertEquals("Name 2", dstName.getValue());
    assertEquals("Description", dstDescription.getValue());
    assertEquals("Name 2", name.getText());
    assertEquals("Description", description.getText());

    // update model
    // act
    model.setName("New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("Description", model.getDescription());
    assertEquals("Description", dstDescription.getValue());
    assertEquals("Description", description.getText());

    // update dst
    // act
    dstDescription.setValue("New Description");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

    // set new src
    // act
    TestModel newModel = new TestModel();
    newModel.setName("New Model Name");
    newModel.setDescription("New Model Description");
    src.setValue(newModel);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model Name", newModel.getName());
    assertEquals("New Model Name", dstName.getValue());
    assertEquals("New Model Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());
    assertEquals("New Model Description", description.getText());

    // update dst when connected to new model
    // act
    dstName.setValue("New Model New Name");
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Model New Name", dstName.getValue());
    assertEquals("New Model New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Model Description", dstDescription.getValue());
    assertEquals("New Model Description", description.getText());

    // set back old model
    // act
    src.setValue(model);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("New Name", dstName.getValue());
    assertEquals("New Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

    // set null src
    // act
    src.setValue(null);
    // assert
    assertEquals("New Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertNull(dstName.getValue());
    assertNull(name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertNull(dstDescription.getValue());
    assertNull(description.getText());

    // set text field value
    // act
    src.setValue(model);
    name.setText("Field Name");
    // assert
    assertEquals("Field Name", model.getName());
    assertEquals("New Model New Name", newModel.getName());
    assertEquals("Field Name", dstName.getValue());
    assertEquals("Field Name", name.getText());
    assertEquals("New Description", model.getDescription());
    assertEquals("New Model Description", newModel.getDescription());
    assertEquals("New Description", dstDescription.getValue());
    assertEquals("New Description", description.getText());

  }
  // endregion
}
