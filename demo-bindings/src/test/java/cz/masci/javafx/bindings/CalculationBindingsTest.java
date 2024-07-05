package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.masci.springfx.mvci.util.constraint.ConditionUtils;
import java.util.concurrent.TimeUnit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.reactfx.value.Val;

public class CalculationBindingsTest {
  // base A + integer property A = A
  // base B + integer property B = B
  // A - B > 0 = success
  // A - B + base C = D
  // success ? (D < 0 ? 1 : D) : 0 = result

  // output: {success, result}

  // region baseAddition
  @Test
  void baseAddition_highLevel() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    NumberBinding dst = Bindings.add(src, base);

    assertEquals(base, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration - base addition [high level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void baseAddition_lowLevel() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    IntegerBinding dst = new AddBaseIntBinding(base, src);

    assertEquals(base, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration - base addition [low level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void baseAddition_reactfx() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    ObservableValue<Integer> dst = Val.map(src, n -> base + n.intValue());

    assertEquals(10, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration - base addition [reactfx]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }
  // endregion

  // region subtraction
  @Test
  void subtraction_highLevel() {
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();
    NumberBinding dst = Bindings.subtract(srcA, srcB);

    assertEquals(0, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    srcA.setValue(20);
    srcB.setValue(10);
    stopWatch.stop();

    assertEquals(10, dst.getValue());
    System.out.println("Duration - subtraction [high level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void subtraction_lowLevel() {
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();
    IntegerBinding dst = new SubIntBinding(srcA, srcB);

    assertEquals(0, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    srcA.setValue(20);
    srcB.setValue(10);
    stopWatch.stop();

    assertEquals(10, dst.getValue());
    System.out.println("Duration - subtraction [low level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void subtraction_reactfx() {
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();

    ObservableValue<Integer> dst = Val.combine(srcA, srcB, (a, b) -> a.intValue() - b.intValue());

    assertEquals(0, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    srcA.setValue(20);
    srcB.setValue(10);
    stopWatch.stop();

    assertEquals(10, dst.getValue());
    System.out.println("Duration - subtraction [reactfx]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }
  // endregion

  // region computation
  @Test
  void computation_highLevel() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();

    // A = base A + src A
    IntegerBinding dstA = srcA.add(baseA);
    // B = base B + src B
    IntegerBinding dstB = srcB.add(baseB);
    // A - B
    NumberBinding result = dstA.subtract(dstB);
    // success = A - B
    BooleanBinding success = result.greaterThan(0);
    IntegerBinding test = Bindings.createIntegerBinding(() -> {
      int add = result.intValue() + baseC;
      return add  < 0 ? 1 : add;
    }, result);
    NumberBinding finalResult = new When(success).then(test).otherwise(0);

    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue(10);
    assertTrue(success.get());
    assertEquals(5, finalResult.getValue());

    srcB.setValue(20);
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue(9);
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue());
    assertEquals(1, result.getValue()
                          .intValue());

    stopWatch.stop();
    System.out.println("Duration - computation [high level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void computation_lowLevel() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();

    // A = base A + src A
    IntegerBinding dstA = srcA.add(baseA);
    // B = base B + src B
    IntegerBinding dstB = srcB.add(baseB);
    // A - B
    NumberBinding result = dstA.subtract(dstB);
    // success = A - B
    BooleanBinding success = result.greaterThan(0);
    ObservableValue<Integer> test = result.add(baseC)
                                          .map(n -> n.intValue() < 0 ? 1 : n.intValue());
    ObservableIntegerValue finalResult = new ConditionalIntegerBinding(success, test, 0);

    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue(10);
    assertTrue(success.get());
    assertEquals(5, finalResult.getValue());

    srcB.setValue(20);
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue(9);
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue());
    assertEquals(1, result.getValue()
                          .intValue());

    stopWatch.stop();
    System.out.println("Duration - computation [low level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void computation_reactfx() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    IntegerProperty srcA = new SimpleIntegerProperty();
    IntegerProperty srcB = new SimpleIntegerProperty();

    // A = base A + src A
    Val<Integer> dstA = Val.map(srcA, n -> n.intValue() + baseA);
    // B = base B + src B
    Val<Integer> dstB = Val.map(srcB, n -> n.intValue() + baseB);
    // A - B
    Val<Integer> result = Val.combine(dstA, dstB, Math::subtractExact);
    // success = A - B
    Val<Boolean> success = result.map(n -> n > 0);
    ObservableValue<Integer> finalResult = success.flatMap(b -> b ? result : null)
                                                  .map(n -> n + baseC)
                                                  .map(n -> n < 0 ? 1 : n)
                                                  .orElseConst(0);

    assertFalse(success.getValue());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue(10);
    assertTrue(success.getValue());
    assertEquals(5, finalResult.getValue());

    srcB.setValue(20);
    assertFalse(success.getValue());
    assertEquals(0, finalResult.getValue());

    srcB.setValue(9);
    assertTrue(success.getValue());
    assertEquals(1, finalResult.getValue());
    assertEquals(1, result.getValue()
                          .intValue());

    stopWatch.stop();
    System.out.println("Duration - computation [reactfx]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }
  // endregion

  // region computation with string

  /**
   * <pre>
   * {@code
   * base A + integer property A = A
   * base B + integer property B = B
   * A - B > 0 = success
   * A - B + base C = D
   * success ? (D < 0 ? 1 : D) : 0 = result
   * output: {valid, success, result}
   * }
   * </pre>
  */
  @Test
  void computation_withString_highLevel() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    StringProperty srcA = new SimpleStringProperty();
    StringProperty srcB = new SimpleStringProperty();

    // src A and src B are valid numbers
    BooleanBinding validComputation = ConditionUtils.isNumber(srcA).and(ConditionUtils.isNumber(srcB));
    // A = base A + src A
    NumberBinding dstA = Bindings.when(ConditionUtils.isNumber(srcA)).then(parseAndAdd(srcA, baseA)).otherwise(0);
    // B = base B + src B
    NumberBinding dstB = Bindings.when(ConditionUtils.isNumber(srcB)).then(parseAndAdd(srcB, baseB)).otherwise(0);
    // result = A - B
    NumberBinding result = dstA.subtract(dstB);
    // success = A - B > 0 when A and B are valid numbers
    BooleanBinding success = Bindings.when(validComputation).then(result.greaterThan(0)).otherwise(false);
    // final result = A - B + base C
    NumberBinding finalResult = Bindings.when(success).then(Bindings.createIntegerBinding(() -> result.intValue() + baseC < 0 ? 1 : result.intValue() + baseC, result)).otherwise(0);

    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue("10");
    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("20");
    assertTrue(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("7");
    assertTrue(validComputation.get());
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue());
    assertEquals(3, result.getValue().intValue());

    stopWatch.stop();
    System.out.println("Duration - computation with string [high level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void computation_withString_lowLevel() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    StringProperty srcA = new SimpleStringProperty();
    StringProperty srcB = new SimpleStringProperty();

    // src A and src B are valid numbers
    BooleanBinding validComputation = new BooleanBinding() {
      {
        super.bind(srcA, srcB);
      }
      @Override
      protected boolean computeValue() {
        return StringUtils.isNumeric(srcA.getValue()) && StringUtils.isNumeric(srcB.getValue());
      }
    };

    // A = base A + src A
    // B = base B + src B
    // result = A - B
    IntegerBinding result = new IntegerBinding() {
      {
        super.bind(validComputation, srcA, srcB);
      }
      @Override
      protected int computeValue() {
        int numA = 0;
        int numB = 0;
        if (validComputation.get()) {
          numA = Integer.parseInt(srcA.getValue()) + baseA;
          numB = Integer.parseInt(srcB.getValue()) + baseB;

        }
        return numA - numB;
      }
    };

    // success = A - B > 0 when A and B are valid numbers
    BooleanBinding success = new BooleanBinding() {
      {
        super.bind(validComputation, result);
      }
      @Override
      protected boolean computeValue() {
        return validComputation.get() && result.intValue() > 0;
      }
    };

    // final result = A - B + base C
    IntegerBinding finalResult = new IntegerBinding() {
      {
        super.bind(success, result);
      }
      @Override
      protected int computeValue() {
        int num = success.get() ? result.intValue() + baseC: 0;
        return num < 0 ? 1 : num;
      }
    };

    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue("10");
    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("20");
    assertTrue(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("7");
    assertTrue(validComputation.get());
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue());
    assertEquals(3, result.getValue().intValue());

    stopWatch.stop();
    System.out.println("Duration - computation with string [low level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void computation_withString_reactfx() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    StringProperty srcA = new SimpleStringProperty();
    StringProperty srcB = new SimpleStringProperty();

    // src A and src B are valid numbers
    Val<Boolean> validComputation = Val.combine(ConditionUtils.isNumber(srcA), ConditionUtils.isNumber(srcB), (a, b) -> a && b);
    // A = base A + src A
    Val<Integer> dstA = validComputation.filter(Boolean.TRUE::equals).flatMap(b -> srcA).map(Integer::parseInt).map(n -> n + baseA).orElseConst(0);
    // B = base B + src B
    Val<Integer> dstB = validComputation.filter(Boolean.TRUE::equals).flatMap(b -> srcB).map(Integer::parseInt).map(n -> n + baseB).orElseConst(0);
    // A - B
    Val<Integer> result = Val.combine(dstA, dstB, Math::subtractExact).orElseConst(0);
    // success = A - B
    Val<Boolean> success = validComputation.filter(Boolean.TRUE::equals).flatMap(b -> result).map(n -> n > 0).orElseConst(false);
    ObservableValue<Integer> finalResult = success.flatMap(b -> b ? result : null)
                                                  .map(n -> n + baseC)
                                                  .map(n -> n < 0 ? 1 : n)
                                                  .orElseConst(0);

    assertFalse(validComputation.getValue());
    assertFalse(success.getValue());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue("10");
    assertFalse(validComputation.getValue());
    assertFalse(success.getValue());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("20");
    assertTrue(validComputation.getValue());
    assertFalse(success.getValue());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("7");
    assertTrue(validComputation.getValue());
    assertTrue(success.getValue());
    assertEquals(1, finalResult.getValue());
    assertEquals(3, result.getValue().intValue());

    stopWatch.stop();
    System.out.println("Duration - computation with string [reactfx level]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  /**
   * <pre>
   * {@code
   * base A + integer property A = A
   * base B + integer property B = B
   * A - B > 0 = success
   * A - B + base C = D
   * success ? (D < 0 ? 1 : D) : 0 = result
   * output: {valid, success, result}
   * }
   * </pre>
   */
  @Test
  void computation_withString_mix() {
    int baseA = 5;
    int baseB = 5;
    int baseC = -5;
    StringProperty srcA = new SimpleStringProperty();
    StringProperty srcB = new SimpleStringProperty();

    // src A and src B are valid numbers
    BooleanBinding validComputation = ConditionUtils.isNumber(srcA).and(ConditionUtils.isNumber(srcB));
    // A = base A + src A
    IntegerBinding dstA = new ParseStringAddBaseBinding(baseA, srcA);
    // B = base B + src B
    IntegerBinding dstB = new ParseStringAddBaseBinding(baseB, srcB);
    // result = A - B
    NumberBinding result = dstA.subtract(dstB);
    // success = A - B > 0 when A and B are valid numbers
    BooleanBinding success = validComputation.and(result.greaterThan(0));
    // final result = A - B + base C
    IntegerBinding finalResult = new IntegerBinding() {
      {
        super.bind(success, result);
      }
      @Override
      protected int computeValue() {
        int num = success.get() ? result.intValue() + baseC: 0;
        return num < 0 ? 1 : num;
      }
    };

    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    srcA.setValue("10");
    assertFalse(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("20");
    assertTrue(validComputation.get());
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue());

    srcB.setValue("7");
    assertTrue(validComputation.get());
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue());
    assertEquals(3, result.getValue().intValue());

    stopWatch.stop();
    System.out.println("Duration - computation with string [mix]: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }


  // endregion

  // region When
  @Test
  void when() {
    BooleanProperty condition = new SimpleBooleanProperty();

    // int binding
    NumberBinding intBinding = new When(condition).then(5)
                                                  .otherwise(6);

    assertEquals(6, intBinding.getValue());
    condition.set(true);
    assertEquals(5, intBinding.getValue());
    condition.set(false);

    // integer property binding
    NumberBinding intPropertyBinding = new When(condition).then(new SimpleIntegerProperty(5))
                                                          .otherwise(new SimpleIntegerProperty());

    assertEquals(0, intPropertyBinding.getValue());
    condition.set(true);
    assertEquals(5, intPropertyBinding.getValue());
    condition.set(false);

    // object property binding to accept null
    ObjectBinding<Integer> objectBinding = new When(condition).then(new SimpleObjectProperty<>(5))
                                                              .otherwise(new SimpleObjectProperty<>());

    assertNull(objectBinding.getValue());
    condition.set(true);
    assertEquals(5, objectBinding.getValue());
    condition.set(false);
  }
  // endregion

  // region utils

  private IntegerBinding parseAndAdd(ObservableStringValue property, Integer base) {
    return Bindings.createIntegerBinding(() -> property.getValue() != null ? Integer.parseInt(property.getValue()) : 0, property).add(base);
  }

  private static final class AddBaseIntBinding extends IntegerBinding {

    private final int base;
    private final IntegerProperty src;

    public AddBaseIntBinding(int base, IntegerProperty src) {
      this.base = base;
      this.src = src;
      super.bind(this.src);
    }

    @Override
    protected int computeValue() {
      return base + src.getValue();
    }
  }

  private static final class ParseStringAddBaseBinding extends IntegerBinding {

    private final int base;
    private final StringProperty src;

    public ParseStringAddBaseBinding(int base, StringProperty src) {
      this.base = base;
      this.src = src;
      super.bind(this.src);
    }

    @Override
    protected int computeValue() {
      int result;
      try {
        result = Integer.parseInt(src.getValue()) + base;
      } catch (NumberFormatException e) {
        result = 0;
      }
      return result;
    }
  }

  private static final class SubIntBinding extends IntegerBinding {

    private final IntegerProperty srcA;
    private final IntegerProperty srcB;

    public SubIntBinding(IntegerProperty srcA, IntegerProperty srcB) {
      this.srcA = srcA;
      this.srcB = srcB;
      super.bind(srcA, srcB);
    }

    @Override
    protected int computeValue() {
      return srcA.getValue() - srcB.getValue();
    }
  }

  private static final class ConditionalIntegerBinding extends IntegerBinding {

    private final ObservableBooleanValue condition;
    private final ObservableValue<Integer> then;
    private final Integer otherwise;

    public ConditionalIntegerBinding(ObservableBooleanValue condition, ObservableValue<Integer> then, Integer otherwise) {
      this.condition = condition;
      this.then = then;
      this.otherwise = otherwise;
      super.bind(condition, then);
    }

    @Override
    protected int computeValue() {
      return condition.get() ? then.getValue() : otherwise;
    }
  }
  // endregion
}
