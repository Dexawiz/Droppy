package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

import com.example.droppy.controller.SignInController;

@SuppressWarnings("unused")
public class LoginController {

    private AuthService authService;
    private Runnable onLoginSuccess;

    public void init(AuthService authService, Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
    }

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Text loginText;

    @FXML
    private Text noAccountText;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signinButton;
    @FXML
    void onLoginButtonClick(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        try {
            authService.login(email, password);
            onLoginSuccess.run();
            loginText.setText("Login Successful " + "Current User: " + email);
        } catch (IllegalArgumentException e) {
            loginText.setText(e.getMessage());
        }

    }

    @FXML
    void onSigninButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignInView.fxml"));
        Parent rootPane = loader.load();

        SignInController signInController = loader.getController();
        Runnable onRegisterSuccess = () -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginController = loginLoader.getController();
                // re-init login controller with the same authService and original onLoginSuccess
                loginController.init(this.authService, this.onLoginSuccess);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                var scene = new Scene(loginRoot);
                stage.setTitle("Droppy");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
        signInController.init(this.authService, onRegisterSuccess);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }
}
