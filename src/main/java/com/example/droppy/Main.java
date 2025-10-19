package com.example.droppy;

import com.example.droppy.controller.LoginController;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.MemoryUserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.Session;
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

        var userDao = new MemoryUserDao();
        var authService = new AuthService(userDao);

        // register default admin
        authService.register("Admin",
                "Admin",
                "admin123@gmail.com",
                "admin123",
                "admin123",
                Role.ADMIN);

        var loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

        // initialize controller with authService
        LoginController controller = loader.getController();
        controller.init(authService, () -> {
        });

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
