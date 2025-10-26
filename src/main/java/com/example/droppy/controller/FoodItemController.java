package com.example.droppy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.server.ExportException;

public class FoodItemController {

    public FoodItemController() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/FoodItemComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        //fxmlLoader.load();
    }

    @FXML
    private Button addItemButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label foodNameLabel;

    @FXML
    private ImageView photoImageView;

}
