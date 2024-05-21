module demo.bindings {
  requires javafx.graphics;
  requires javafx.base;
  requires commons.springfx.mvci;
  requires dirtyfx;
  requires annotations;
  requires MaterialFX;
  requires reactfx;
  requires testfx.core;

  exports cz.masci.javafx.bindings to javafx.graphics;

  opens cz.masci.javafx.bindings;
}