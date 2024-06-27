module demo.wizard {
  requires javafx.controls;
  requires javafx.graphics;
  requires commons.springfx.mvci;
  requires dirtyfx;
  requires annotations;
  requires MaterialFX;
  requires reactfx;
  requires static lombok;
  requires static org.slf4j;

  exports cz.masci.javafx.demo.wizard to javafx.graphics;
}