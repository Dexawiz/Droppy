package com.example.droppy.controller;

import com.example.droppy.Navigator.Navigator;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.UserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.util.Locale;

public class SaveDriverController {

    AuthService authService;
    private Runnable onRegisterSuccess;

    public void init(AuthService authService, Runnable onRegisterSuccess) {
        this.onRegisterSuccess = onRegisterSuccess;
        this.authService = authService;

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();
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
            authService.register(driverName, driverSurname, driverEmail, driverPhoneNumber, driverPassword, driverPassword, Role.DRIVER);
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

    private void updateText(){
        driverLabel.setText(I18n.get("driver"));
        saveDriverButton.setText(I18n.get("save"));
        returnToAddDriver.setText(I18n.get("return"));
        driverNameTextField.promptTextProperty().set(I18n.get("name"));
        drverSurnameTextField.promptTextProperty().set(I18n.get("surname"));
        driverPhoneNumberTextField.promptTextProperty().set(I18n.get("phone_number"));
        driverPasswordField.promptTextProperty().set(I18n.get("passwd"));
    }

}
