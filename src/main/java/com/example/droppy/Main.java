package com.example.droppy;

import com.example.droppy.controller.LoginController;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.AuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        var userDao = new HibernateUserDao();
        var authService = new AuthService(userDao);

        // register default admin
        if (userDao.findByEmail("admin@droppy.com") == null) {
            authService.register(
                    "Admin",
                    "Root",
                    "admin@droppy.com",
                    "admin123",
                    "admin123",
                    Role.ADMIN
            );
        }

        var loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

        // initialize controller with authService
        LoginController controller = loader.getController();
        controller.init(authService, () -> {});

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
