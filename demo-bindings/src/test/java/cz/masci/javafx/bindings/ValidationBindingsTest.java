package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.palexdev.mfxcore.validation.Constraint;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;
import org.reactfx.value.Val;
import org.reactfx.value.Var;

public class ValidationBindingsTest {

  @Test
  void validation() {
    ValidTestModel srcModel = new ValidTestModel();
    Var<ValidTestModel> src = Var.newSimpleVar(null);
    Val<Boolean> validProperty = src.flatMap(model -> model.getValidator().validProperty());

    assertFalse(validProperty.getOrElse(false));

    Constraint constraint = Constraint.of("test", Bindings.createBooleanBinding(() -> validProperty.getOrElse(false), validProperty));

    assertFalse(constraint.isValid());

    // set valid model to src
    srcModel.setName("name");
    srcModel.setAge(ValidTestModel.MIN_AGE);
    src.setValue(srcModel);
    assertTrue(constraint.isValid());

    // invalidate the model
    srcModel.setAge(ValidTestModel.MIN_AGE - 5);
    assertFalse(constraint.isValid());

    // set null to src
    src.setValue(null);
    assertFalse(constraint.isValid());

    // set invalid model to src
    srcModel.setAge(ValidTestModel.MIN_AGE);
    src.setValue(srcModel);

    assertTrue(constraint.isValid());

    // set null to src again when it was valid
    src.setValue(null);
    assertFalse(constraint.isValid());
  }

  @Test
  void validation_from_observable() {
    ValidTestModel srcModel = new ValidTestModel();
    ObjectProperty<ValidTestModel> srcObjectProperty = new SimpleObjectProperty<>();
    Val<ValidTestModel> src = Val.wrap(srcObjectProperty);
    Val<Boolean> validProperty = src.flatMap(model -> model.getValidator().validProperty());

    assertFalse(validProperty.getOrElse(false));

    Constraint constraint = Constraint.of("test", Bindings.createBooleanBinding(() -> validProperty.getOrElse(false), validProperty));

    assertFalse(constraint.isValid());

    // set valid model to src
    srcModel.setName("name");
    srcModel.setAge(ValidTestModel.MIN_AGE);
    srcObjectProperty.setValue(srcModel);
    assertTrue(constraint.isValid());

    // invalidate the model
    srcModel.setAge(ValidTestModel.MIN_AGE - 5);
    assertFalse(constraint.isValid());

    // set null to src
    srcObjectProperty.setValue(null);
    assertFalse(constraint.isValid());

    // set invalid model to src
    srcModel.setAge(ValidTestModel.MIN_AGE);
    srcObjectProperty.setValue(srcModel);

    assertTrue(constraint.isValid());

    // set null to src again when it was valid
    srcObjectProperty.setValue(null);
    assertFalse(constraint.isValid());
  }
}
