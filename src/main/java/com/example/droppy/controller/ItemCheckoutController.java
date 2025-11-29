package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ItemCheckoutController {

    private AuthService authService;
    private Order order;
    private OrderItem item;


    public void init(AuthService auth, Order order, OrderItem item) {
        this.authService = auth;
        this.order = order;
        this.item = item;

        ItemNameLabel.setText(item.getProduct().getName());
        priceDemoLabel.setText(String.valueOf(item.getPricePerItem()));
        numberOfItemsLabel.setText(String.valueOf(item.getQuantity()));
    }

    @FXML
    private Label ItemNameLabel;

    @FXML
    private Button addItemButton;

    @FXML
    private Text decriptionText;

    @FXML
    private Button deleteItemButton;

    @FXML
    private Label eurLabel;

    @FXML
    private Label numberOfItemsLabel;

    @FXML
    private Label priceDemoLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label xLabel;

    @FXML
    void onAddItemLabelButtonCLick(ActionEvent event) {

    }

    @FXML
    void onDeleteItemButtonClick(ActionEvent event) {

    }

}
