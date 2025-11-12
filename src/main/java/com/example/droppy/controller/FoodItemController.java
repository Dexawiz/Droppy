package com.example.droppy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private Label ItemNameLabel;

    @FXML
    private Button addItemButton;

    @FXML
    private Text decriptionText;

    @FXML
    private Label eurLabel;

    @FXML
    private Label priceDemoLabel;

    @FXML
    private Label priceLabel;

    @FXML
    void onAddItemLabelButtonCLick(ActionEvent event) {

    }


}
