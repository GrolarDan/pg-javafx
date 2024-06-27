package cz.masci.javafx.bindings;

import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import java.util.List;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.nield.dirtyfx.tracking.CompositeDirtyProperty;
import org.nield.dirtyfx.tracking.DirtyProperty;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import org.reactfx.value.Var;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private Subscription selectedPropertySubscription;

  @Override
  public void start(Stage stage) {
    ObservableList<Model> models = FXCollections.observableArrayList();
    models.addAll(
        IntStream.range(0, 100)
            .mapToObj(i -> {
              var model = new Model();
              model.setName(String.format("Name %d", i));
              model.setDescription(String.format("Description %d", i));
              model.rebaseline();
              return model;
            })
            .toList()
    );

    MFXTableColumn<Model> nameColumn = new MFXTableColumn<>("Name");
    nameColumn.setRowCellFactory(model -> new MFXTableRowCell<>(Model::getName));
    MFXTableColumn<Model> descriptionColumn = new MFXTableColumn<>("Description");
    descriptionColumn.setRowCellFactory(model -> new MFXTableRowCell<>(Model::getDescription));
    MFXTableView<Model> tableView = new MFXTableView<>(models);
    tableView.setMaxWidth(Double.MAX_VALUE);
    tableView.setMaxHeight(Double.MAX_VALUE);
    tableView.getTableColumns().addAll(List.of(nameColumn, descriptionColumn));
    tableView.getSelectionModel().setAllowsMultipleSelection(false);

    ObservableMap<Integer, Model> selectionProperty = tableView.getSelectionModel().selectionProperty();
    Var<Model> selectedProperty = Var.newSimpleVar(new Model());
//    selectedProperty.addListener((observableValue, oldValue, newValue) -> System.out.format("Selected property changed to %s with name %s and dirty %s (dirty of name = %s)\n", newValue, newValue.getName(), newValue.isDirty(), newValue.nameProperty().isDirty()));
    selectedProperty.flatMap(Model::isDirtyProperty).addListener((obs, oldValue, newValue) -> {
      System.out.format("Dirty property of selected property %s has changed to %s\n", selectedProperty.getValue().getName(), newValue);
      tableView.update();
    });
    Var<String> nameProperty = selectedProperty.selectVar(Model::nameProperty);
    Var<String> descriptionProperty = selectedProperty.selectVar(Model::descriptionProperty);

    selectedPropertySubscription = EventStreams.changesOf(selectionProperty)
        .filter(Change::wasAdded)
        .map(Change::getValueAdded)
        .hook(value -> System.out.format("Added model with name %s\n", value.getName()))
        .feedTo(selectedProperty);

    TextField name = new TextField();
    name.setMaxWidth(Double.MAX_VALUE);
    name.textProperty().bindBidirectional(nameProperty);
    nameProperty.addListener((obs, oldValue, newValue) -> {
      System.out.println("Name property has changed");
      name.getText();
      tableView.update();
    });
    TextField description = new TextField();
    description.setMaxWidth(Double.MAX_VALUE);
    description.textProperty().bindBidirectional(descriptionProperty);
    descriptionProperty.addListener((obs, oldValue, newValue) -> {
      System.out.println("Description property has changed");
      description.getText();
      tableView.update();
    });
    VBox vbox = new VBox(name, description, tableView);
    vbox.setPadding(new Insets(10.0));
    vbox.setSpacing(10.0);

    stage.setScene(new Scene(vbox, 600, 480));
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    selectedPropertySubscription.unsubscribe();
  }

  private static class Model implements DirtyProperty {
    private final DirtyStringProperty name = new DirtyStringProperty("");
    private final DirtyStringProperty description = new DirtyStringProperty("");
    private final CompositeDirtyProperty compositeDirty = new CompositeDirtyProperty();

    public Model() {
      compositeDirty.addAll(name, description);
    }

    public String getName() {
      return name.get();
    }

    public DirtyStringProperty nameProperty() {
      return name;
    }

    public void setName(String name) {
      this.name.set(name);
    }

    public String getDescription() {
      return description.get();
    }

    public DirtyStringProperty descriptionProperty() {
      return description;
    }

    public void setDescription(String description) {
      this.description.set(description);
    }

    @Override
    public boolean isDirty() {
      return compositeDirty.isDirty();
    }

    @NotNull
    @Override
    public ObservableValue<Boolean> isDirtyProperty() {
      return compositeDirty.isDirtyProperty();
    }

    @Override
    public void rebaseline() {
      compositeDirty.rebaseline();
    }

    @Override
    public void reset() {
      compositeDirty.reset();
    }
  }
}
