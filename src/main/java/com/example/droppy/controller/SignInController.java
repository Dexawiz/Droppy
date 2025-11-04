package com.example.droppy.controller;

import com.example.droppy.domain.enums.Role;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {


    private AuthService authService;
    private Runnable onRegisterSuccess;

    public void init(AuthService authService, Runnable onRegisterSuccess) {
        this.authService = authService;
        this.onRegisterSuccess = onRegisterSuccess;
    }

    @FXML
    private Button backButton;

    @FXML
    private PasswordField confirmPasswordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    void onSignInButtonClick(ActionEvent event) {
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signInText.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            signInText.setText("Passwords do not match.");
            return;
        }

        try {
            authService.register(name, surname, email, password, confirmPassword, Role.CUSTOMER);
            signInText.setText("Registration successful! You can now log in.");

            if (onRegisterSuccess != null) {
                onRegisterSuccess.run();
            }
        } catch (Exception e) {
            signInText.setText("Registration failed: " + e.getMessage());
        }

    }

    @FXML
    private Text signInText;

    @FXML
    private TextField surnameTextField;
    @FXML
    void onBackButtonClick(ActionEvent event) throws Exception {
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
