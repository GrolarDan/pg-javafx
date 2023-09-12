module cz.masci.javafx.homescreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;

    opens cz.masci.javafx.homescreen to javafx.fxml;
    exports cz.masci.javafx.homescreen;
  exports cz.masci.javafx.homescreen.controller;
  opens cz.masci.javafx.homescreen.controller to javafx.fxml;
}