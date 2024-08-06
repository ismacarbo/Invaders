package com.example.invaders.Main;

import View.GameView;
import com.example.invaders.Controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GameView view = new GameView(canvas);
        GameController controller = new GameController(view);  // Inizializza il controller subito

        Scene scene = new Scene(new javafx.scene.Group(canvas));
        controller.bindScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
