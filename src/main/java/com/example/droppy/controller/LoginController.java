package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import com.example.droppy.service.Session;
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
            if (onLoginSuccess != null) onLoginSuccess.run();
            loginText.setText("Login Successful " + "Current User: " + email );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent rootPane = loader.load();

            HomeController controller = loader.getController();
            controller.init(authService);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        } catch (IllegalArgumentException e) {
            loginText.setText(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onSigninButtonClick(ActionEvent event) throws IOException {
        // получаем текущий Stage заранее, чтобы передать в колбэк
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignInView.fxml"));
        Parent rootPane = loader.load();


        SignInController signInController = loader.getController();
        signInController.init(authService, () -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginCtrl = loginLoader.getController();
                loginCtrl.init(authService, this.onLoginSuccess);
                var scene = new Scene(loginRoot);
                stage.setTitle("Droppy");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }
}
