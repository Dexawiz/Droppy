package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TextField confirmPasswordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    void onSignInButtonClick(ActionEvent event) {
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        try {
            authService.register(name, surname, email, password, confirmPassword, true);
            onRegisterSuccess.run();
        } catch (IllegalArgumentException e) {
            signInText.setText(e.getMessage());
        }
    }


    @FXML
    private Text signInText;

    @FXML
    private TextField surnameTextField;

    @FXML
    void onBackButtonClick(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

//        // initialize login controller with the same authService and a simple onLoginSuccess handler
//        LoginController loginController = loader.getController();
//        loginController.init(this.authService, () -> System.out.println("Login successful"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }

}
