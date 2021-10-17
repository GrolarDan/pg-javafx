package cz.masci.javafx.demo.abstractdetail;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class AbstractDetailController<T> {

  private BooleanProperty modified;
  private List<ObservableValue<String>> observableValues;
  private ChangeListener<String> listener;
  private T item;

  public void setItem(T item) {
    if (this.item != null) {
      unhookListener();
    }
    this.item = item;
    hookTo(item);
  }

  public T getItem() {
    return item;
  }
  
  public BooleanProperty modifiedProperty() {
    if (modified == null) {
      modified = new SimpleBooleanProperty(false);
    }    
    return modified;
  }
  
  protected void setModified(boolean modified) {
    modifiedProperty().set(modified);
  }
  
  protected boolean isModified() {
    return modifiedProperty().get();
  }
  
  private void unhookListener() {
    getObservableValues().forEach(t -> t.removeListener(listener));
    setModified(false);
  }

  private void hookTo(T item) {
    fillInputs(item);
    if (item == null) {
      listener = null;
    } else {
      listener = (observable, oldValue, newValue) -> {
        changed(observable, oldValue, newValue);
        setModified(true);
      };
      getObservableValues().forEach(t -> t.addListener(listener));
    }
  }

  private List<ObservableValue<String>> getObservableValues() {
    if (observableValues == null) {
      observableValues = List.copyOf(initObservableValues());
    }
    return observableValues;
  }
  
  protected abstract List<ObservableValue<String>> initObservableValues();
  
  protected abstract void fillInputs(T item);
  
  protected abstract void changed​(ObservableValue<? extends String> observable, String oldValue, String newValue);
}
