module org.openjfx.helloworldfxml {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.helloworldfxml to javafx.fxml;
    exports org.openjfx.helloworldfxml;
}