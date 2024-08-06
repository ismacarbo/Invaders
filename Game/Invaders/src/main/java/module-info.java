module com.example.invaders {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.example.invaders.Main;
    opens com.example.invaders.Main to javafx.graphics;
}
