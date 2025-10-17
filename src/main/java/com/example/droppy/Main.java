package com.example.droppy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        Button btn = new Button("Click me");
//        btn.setOnAction(e -> btn.setText("Hello, Droppy!"));
//
//        StackPane root = new StackPane(btn);
//        Scene scene = new Scene(root, 400, 300);
//
//        stage.setTitle("Droppy");
//        stage.setScene(scene);
//        stage.show();
        var loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

        var scene = new Scene(rootPane);
        stage.setTitle("Log in");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

