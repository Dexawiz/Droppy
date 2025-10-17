package com.example.droppy.controller;

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

    @FXML
    private Button backButton;

    @FXML
    private TextField comfirmPasswordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    private Text signInText;

    @FXML
    private TextField surnameTextField;

    @FXML
    void onBackButtonClick(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }

}
