package com.example.droppy.controller;

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

@SuppressWarnings("unused")
public class LoginController {

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
    void onSigninButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignInView.fxml"));
        Parent rootPane = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }
}
