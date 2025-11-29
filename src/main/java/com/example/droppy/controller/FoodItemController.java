package com.example.droppy.controller;

import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.service.CartService;
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
import java.net.URL;
import java.rmi.server.ExportException;

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

    private CartService cartService;
    private Product product;

    @FXML
    void onAddItemLabelButtonCLick(ActionEvent event) {
            //cartService.add(new OrderItem(product, 1));
    }

        public void init (Product product, CartService cartService){
            this.product = product;
            this.cartService = cartService;

            ItemNameLabel.setText(product.getName());
            decriptionText.setText(product.getDescription());
            priceDemoLabel.setText(String.valueOf(product.getPrice()));

            URL css = getClass().getResource("/styles/FoodItemCStyle.css.css");
            if (css != null) {
                mainPane.getStylesheets().add(css.toExternalForm());
            }
        }
    }

