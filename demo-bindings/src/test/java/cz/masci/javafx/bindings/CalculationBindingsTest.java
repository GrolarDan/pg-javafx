package cz.masci.javafx.bindings;

public class CalculationBindingsTest {
  // base A + integer property A = A
  // base B + integer property B = B
  // A - B > 0 = success
  // A - B + base C = D
  // success ? (D < 0 ? 1 : D) : 0 = result

  // output: {success, result}
}
