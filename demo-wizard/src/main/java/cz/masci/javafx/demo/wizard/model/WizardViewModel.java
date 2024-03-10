package cz.masci.javafx.demo.wizard.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WizardViewModel {
  private final BooleanProperty prevDisable = new SimpleBooleanProperty(false);
  private final BooleanProperty nextDisable = new SimpleBooleanProperty(false);
  private final StringProperty prevText = new SimpleStringProperty("");
  private final StringProperty nextText = new SimpleStringProperty("");
  private final StringProperty title = new SimpleStringProperty("");

  // region properties
  public BooleanProperty prevDisableProperty() {
    return prevDisable;
  }

  public BooleanProperty nextDisableProperty() {
    return nextDisable;
  }

  public StringProperty prevTextProperty() {
    return prevText;
  }

  public StringProperty nextTextProperty() {
    return nextText;
  }

  public StringProperty titleProperty() {
    return title;
  }
}
