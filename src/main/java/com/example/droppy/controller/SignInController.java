package com.example.droppy.controller;

import com.example.droppy.domain.enums.Role;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import com.example.droppy.Navigator.*;
import java.util.Locale;

public class SignInController {


    private AuthService authService;
    private Runnable onRegisterSuccess;

    public void init(AuthService authService, Runnable onRegisterSuccess) {
        this.authService = authService;
        this.onRegisterSuccess = onRegisterSuccess;
        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();
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
        Navigator.switchTo(
                event,
                "/LoginView.fxml",
                "Droppy",
                LoginController.class,
                c -> c.init(authService, () -> {})
        );
    }

    private void updateText(){
        signInText.setText(I18n.get("signin"));
        signInButton.setText(I18n.get("signin"));
        nameTextField.promptTextProperty().set(I18n.get("name"));
        surnameTextField.promptTextProperty().set(I18n.get("surname"));
        passwordTextField.promptTextProperty().set(I18n.get("passwd"));
        confirmPasswordTextField.promptTextProperty().set(I18n.get("comfirm_passwd"));
    }
}
