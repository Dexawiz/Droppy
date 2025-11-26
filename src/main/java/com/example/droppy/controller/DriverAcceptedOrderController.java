package com.example.droppy.controller;

import com.example.droppy.domain.entity.User;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DriverAcceptedOrderController{
    private AuthService authService;

    public  void init (AuthService authService){
        this.authService = authService;

        User user  = authService.getCurrentUser();
        if(user != null){
            nameDriverLabel.setText(user.getName());
            surnameDriverLabel.setText(user.getSurname());
        }
    }

    @FXML
    private Button AcOrderButton;

    @FXML
    private Button AvOrderButton;

    @FXML
    private Button DeOrderButton;

    @FXML
    private Label acceptedOrderLabel;

    @FXML
    private VBox column1VBOX;

    @FXML
    private ChoiceBox<?> driverStatusCB;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private HBox header;

    @FXML
    private Button logOutButton;

    @FXML
    private Label nameDriverLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label surnameDriverLabel;

    @FXML
    void onAcOrderButtonClick(ActionEvent event) {

    }

    @FXML
    void onAvOrderButtonClick(ActionEvent event) {

    }

    @FXML
    void onDeOrderButtonClick(ActionEvent event) {

    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws IOException {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController loginController = loader.getController();
            if (this.authService != null) {
                loginController.init(this.authService, () -> {});
            }

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }

    }

}
