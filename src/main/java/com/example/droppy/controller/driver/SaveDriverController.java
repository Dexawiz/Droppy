package com.example.droppy.controller.driver;

import com.example.droppy.controller.admin.AdminDriversController;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
            Parent root = loader.load();

            AdminDriversController controller = loader.getController();
            controller.init(authService, AdminDriversController.Mode.LIST_ALL);

            Stage stage = (Stage) returnToAddDriver.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("droppy - Drivers");
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException("Failed to switch view: /AdminDriversView.fxml", e);
        }
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
