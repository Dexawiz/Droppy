package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodItemController {
    private Product product;
    private AuthService authService;
    private Order currentOrder;

    public void init(AuthService authService, Product product, Order currentOrder) {
        this.currentOrder = currentOrder;
        this.product = product;
        this.authService = authService;

        ItemNameLabel.setText(product.getName());
        decriptionText.setText(product.getDescription());
        priceDemoLabel.setText(String.valueOf(product.getPrice()));

        URL css = getClass().getResource("/styles/FoodItemCStyle.css.css");
        if (css != null) {
            mainPane.getStylesheets().add(css.toExternalForm());
        }
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
    private VBox mainPane;


    @FXML
    void onAddItemLabelButtonCLick(ActionEvent event) {
        OrderDao orderDao = new HibernateOrderDao();

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1);
        orderItem.setPricePerItem(product.getPrice());

        Order tempOrder = new Order();
        tempOrder.setId(currentOrder.getId());
        tempOrder.setOrderItems(List.of(orderItem));

        currentOrder = orderDao.add(tempOrder);

        currentOrder.setDeliveryFromAddress(product.getCompany().getAddress());
        String address = authService.getCurrentUser().getAddress().getStreet() + ", "
                + authService.getCurrentUser().getAddress().getPostalCode() + " "
                + authService.getCurrentUser().getAddress().getCity() + ", "
                + authService.getCurrentUser().getAddress().getCountry();
        currentOrder.setDeliveryToAddress(address);
        double total = currentOrder.getOrderItems().stream()
                .mapToDouble(item -> item.getPricePerItem() * item.getQuantity())
                .sum();
        currentOrder.setTotalPrice(total);
        currentOrder.setOrderCreatedTime(java.time.LocalDateTime.now());
        currentOrder.setPaymentMethod(MethodOfPayment.ONLINE);
        orderDao.update(currentOrder);
        addItemButton.setDisable( true );
    }
}