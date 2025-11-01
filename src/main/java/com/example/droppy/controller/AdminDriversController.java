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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AdminDriversController {

    @FXML
    private Button addDriverButton;

    @FXML
    private Button deleteDriverButton;

    @FXML
    private ListView<?> driversListView;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToCompaniesButton;
    private AuthService authService;


    public void init(AuthService authService) {
        this.authService = authService;
    }
    @FXML
    void addDriverButtonAction(ActionEvent event) {

    }

    @FXML
    void driversListViewMouseClicked(MouseEvent event) {

    }

    @FXML
    void deleteDriverButtonClick(ActionEvent event) {

    }

    @FXML
    void onCompaniesSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
        Parent rootPane = loader.load();

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy");
        stage.show();
    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws Exception {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();
        if (result.get() == javafx.scene.control.ButtonType.OK) {
            authService.logout();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController controller = loader.getController();
            controller.init(authService, () -> {});

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }

    }

}
