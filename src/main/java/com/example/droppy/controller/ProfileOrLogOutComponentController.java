package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ProfileOrLogOutComponentController {

    private Stage mainStage;
    private AuthService authService;
    public void init(AuthService authService, Stage mainStage) {
        this.authService = authService;
        this.mainStage = mainStage;
    }


    @FXML
    private Button LogOutButton;

    @FXML
    private Button ProfileButton;

    @FXML
    void onLogOutButtonClick(ActionEvent event) throws Exception{
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();

        if ((result.get() == javafx.scene.control.ButtonType.OK)) {
            authService.logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            Scene scene = new Scene(rootPane);
            mainStage.setScene(scene);
            mainStage.setTitle("Droppy");
            mainStage.show();
        }
    }

    @FXML
    void onProfileButtonClick(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileView.fxml"));
        Parent rootPane = loader.load();

        Scene scene = new Scene(rootPane);
        mainStage.setScene(scene);
        mainStage.setTitle("Droppy");
        mainStage.show();

    }

}




