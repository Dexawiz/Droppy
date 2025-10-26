package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

public class HomeController {

    private AuthService authService;

    public void init(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label categoriesLabel;

    @FXML
    private CheckBox categoryItemCheckBox;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Label restaurantsLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    void onCategoryItemCheckBoxClicked(ActionEvent event) {

    }


}
