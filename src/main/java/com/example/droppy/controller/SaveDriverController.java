package com.example.droppy.controller;

import com.example.droppy.Navigator.Navigator;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;

public class SaveDriverController {

    AuthService authService;
    private Runnable onRegisterSuccess;

    public void init(AuthService authService, Runnable onRegisterSuccess) {
        this.onRegisterSuccess = onRegisterSuccess;
        this.authService = authService;
    }

    @FXML
    private Label driverLabel;

    @FXML
    private Button returnToAddDriver;

    @FXML
    private TextField driverNameTextField;

    @FXML
    private TextField driverPasswordField;

    @FXML
    private TextField driverPhoneNumberTextField;

    @FXML
    private TextField drverSurnameTextField;

    @FXML
    private TextField emailDriverTextField;

    @FXML
    private Button saveDriverButton;

    @FXML
    void onSaveDriverButtonClick(ActionEvent event) {
        String driverName = driverNameTextField.getText();
        String driverSurname = drverSurnameTextField.getText();
        String driverEmail = "";
        String driverPhoneNumber = driverPhoneNumberTextField.getText();
        String driverPassword = driverPasswordField.getText();

        if(driverName.isEmpty() || driverSurname.isEmpty()  || driverPhoneNumber.isEmpty() || driverPassword.isEmpty()) {
            driverLabel.setText("Please fill in all fields.");
            return;
        }

        driverEmail = driverName + "." + driverSurname + ".driver" + "@droppy.com";


        try {
            authService.register(driverName, driverSurname, driverEmail, driverPassword, driverPassword, Role.DRIVER);
            driverLabel.setText("Registration successful! You can now log in.");

            if (onRegisterSuccess != null) {
                onRegisterSuccess.run();
            }
        } catch (Exception e) {
            driverLabel.setText("Registration failed: " + e.getMessage());
        }

    }

    @FXML
    void returnToAddDriverButton(ActionEvent event) {
        Navigator.switchTo(
                event,
                "/AdminDriversView.fxml",
                "droppy - Add Driver",
                AdminDriversController.class,
                controller -> controller.init(authService,
                        AdminDriversController.Mode.LIST_ALL)
        );
    }

}
