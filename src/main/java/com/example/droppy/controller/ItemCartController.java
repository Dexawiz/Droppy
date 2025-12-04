package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ItemCartController {

    private AuthService authService;
    private Order order;
    private OrderItem item;
    private OrderDao orderDao;
    private Runnable refreshCallback;

    public void init(AuthService auth, Order order, OrderItem item, Runnable refreshCallback) {
        this.authService = auth;
        this.order = order;
        this.item = item;
        this.refreshCallback = refreshCallback;


        this.orderDao = new HibernateOrderDao();
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
        item.setQuantity(item.getQuantity() + 1);
        numberOfItemsLabel.setText(String.valueOf(item.getQuantity()));
        order.setTotalPrice( order.getTotalPrice() + item.getPricePerItem());
        order = orderDao.updateOI(order);
        refreshCallback.run();
    }



    @FXML
    void onDeleteItemButtonClick(ActionEvent event) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            order.getOrderItems().remove(item);
        }

        double total = order.getOrderItems().stream()
                .mapToDouble(i -> i.getPricePerItem() * i.getQuantity())
                .sum();
        order.setTotalPrice(total);

        order = orderDao.updateOI(order);
        refreshCallback.run();
    }

}
