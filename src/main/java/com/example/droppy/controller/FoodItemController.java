package com.example.droppy.controller;

import com.example.droppy.domain.entity.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;

public class FoodItemController {

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
    private VBox mainPane;

    private Product product;

    @FXML
    void onAddItemLabelButtonCLick(ActionEvent event) {
            //cartService.add(new OrderItem(product, 1));
    }

        public void init (Product product){
            this.product = product;

            ItemNameLabel.setText(product.getName());
            decriptionText.setText(product.getDescription());
            priceDemoLabel.setText(String.valueOf(product.getPrice()));

            URL css = getClass().getResource("/styles/FoodItemCStyle.css.css");
            if (css != null) {
                mainPane.getStylesheets().add(css.toExternalForm());
            }
        }
    }

