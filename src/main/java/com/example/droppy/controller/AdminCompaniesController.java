package com.example.droppy.controller;

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

import java.io.IOException;

public class AdminCompaniesController {

    @FXML
    private Button addCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private ListView<?> driversListView;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToDriversButton;

    @FXML
    void addCompanyButtonAction(ActionEvent event) {

    }

    @FXML
    void attendancesListViewMouseClicked(MouseEvent event) {

    }

    @FXML
    void deleteCompanyButtonClick(ActionEvent event) {

    }

    @FXML
    void onDriversSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
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
        if ((result.get() == javafx.scene.control.ButtonType.OK)) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }
    }
}
