package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
    ObservableValue<Integer> zero = new SimpleObjectProperty<>(0);

    // A = base A + src A
    IntegerBinding dstA = srcA.add(baseA);
    // B = base B + src B
    IntegerBinding dstB = srcB.add(baseB);
    // A - B
    NumberBinding result = dstA.subtract(dstB);
    // success = A - B
    BooleanBinding success = result.greaterThan(0);
    ObservableValue<Integer> test = result.add(baseC).map(n -> n.intValue() < 0 ? 1 : n.intValue());
    ObjectBinding<ObservableValue<Integer>> finalResult = new When(success).then(test).otherwise(zero);

    assertFalse(success.get());
    assertEquals(0, finalResult.getValue().getValue());

    srcA.setValue(10);
    assertTrue(success.get());
    assertEquals(5, finalResult.getValue().getValue());

    srcB.setValue(20);
    assertFalse(success.get());
    assertEquals(0, finalResult.getValue().getValue());

    srcB.setValue(9);
    assertTrue(success.get());
    assertEquals(1, finalResult.getValue().getValue());
    assertEquals(1, result.getValue().intValue());
  }
  // endregion

  // region utils
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


  // endregion
}
