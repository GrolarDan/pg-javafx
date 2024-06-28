package cz.masci.javafx.bindings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

  @Test
  void baseAddition_highLevel() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    NumberBinding dst = Bindings.add(base, src);

    assertEquals(base, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void baseAddition_lowLevel() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    IntegerBinding dst = new AddIntBinding(base, src);

    assertEquals(base, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration: " + stopWatch.getTime(TimeUnit.NANOSECONDS));
  }

  @Test
  void baseAddition_reactfx() {
    int base = 10;
    IntegerProperty src = new SimpleIntegerProperty();
    IntegerProperty dst = new SimpleIntegerProperty();

    var subscription = Val.wrap(src)
        .map(n -> base + n.intValue())
        .values()
        .feedTo(dst);

    assertEquals(10, dst.getValue());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    src.setValue(20);
    stopWatch.stop();

    assertEquals(30, dst.getValue());
    System.out.println("Duration: " + stopWatch.getTime(TimeUnit.NANOSECONDS));

    subscription.unsubscribe();
  }

  private static final class AddIntBinding extends IntegerBinding {

    private final int base;
    private final IntegerProperty src;

    public AddIntBinding(int base, IntegerProperty src) {
      this.base = base;
      this.src = src;
      super.bind(this.src);
    }

    @Override
    protected int computeValue() {
      return base + src.getValue();
    }
  }
}
