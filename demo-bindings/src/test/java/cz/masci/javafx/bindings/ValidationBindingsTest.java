package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
  }
}
