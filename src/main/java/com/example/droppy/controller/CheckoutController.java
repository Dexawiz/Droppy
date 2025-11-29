package com.example.droppy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

public class CheckoutController {

    @FXML
    private Label OTDemo;

    @FXML
    private Button OrderButton;

    @FXML
    private Label OrderTotalLabel;

    @FXML
    private Label PNLabel;

    @FXML
    private TextField PNTextField;

    @FXML
    private Label PriceLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Label nameDemo;

    @FXML
    private ScrollPane orderListScrollPane;

    @FXML
    private Label productsLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label serviceFee;

    @FXML
    private Label serviceFeeLabel;

    @FXML
    private FontIcon shoppingCartIcon;

    @FXML
    private Label surnameDemo;

    @FXML
    private Label totalDemo;

    @FXML
    private Label totalLabel;

    @FXML
    private Label userInfoLabel;

    @FXML
    void onOrderButtonClick(ActionEvent event) {

    }

    @FXML
    void onProfileClick(MouseEvent event) {

    }

}
