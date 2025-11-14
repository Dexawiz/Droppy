package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

public class CompanyNameController {

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label categoryNameLabel;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Label restaurantNameLabel;

    @FXML
    private TextField searchTextField;

    public void init(Company company) {
        restaurantNameLabel.setText(company.getName());
        categoryNameLabel.setText(company.getCategory().name());
    }
}
