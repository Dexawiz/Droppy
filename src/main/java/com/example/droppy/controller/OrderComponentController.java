package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.HibernateOrderItemDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OrderComponentController {
    private OrderDao orderDao;
    private Order order;
    private AuthService authService;
    HibernateOrderItemDao orderItemDao = new HibernateOrderItemDao();

    public void init(AuthService authService, Order order) {
        this.order = order;
        orderDao = new HibernateOrderDao();
        this.authService = authService;
        orderIDDemo.setText(String.valueOf(order.getId()));
        nameCompanyDemo.setText(order.getCompanyId().getName());
        addressCompanyDemo.setText(order.getCompanyId().getAddress());
        addressToDeliverDemo.setText(order.getDeliveryToAddress());
        priceDemo.setText(String.format("%.2f", order.getTotalPrice()));

        var orderItems = orderItemDao.findByOrderId(order.getId());

        String productsText = orderItems.stream()
                .map(oi -> oi.getProduct().getName() + " x" + oi.getQuantity())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        productToDeliverDemo.setText(productsText);
    }


    @FXML
    private Label CompanyLabel;

    @FXML
    private Label EURLabel;

    @FXML
    private Button addButton;

    @FXML
    private Label addressCompanyDemo;

    @FXML
    private Label addressToDeliverDemo;

    @FXML
    private Label addressToDeliverLabel;

    @FXML
    private Label companyAddressLabel;

    @FXML
    private Label nameCompanyDemo;

    @FXML
    private Label orderIDDemo;

    @FXML
    private Label orderIDLabel;

    @FXML
    private Label priceDemo;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productToDeliverDemo;

    @FXML
    private Label productToDeliverLabel;

    @FXML
    void onAddButtonClick(ActionEvent event) {
        orderDao.updateOrderStatus(order.getId(), com.example.droppy.domain.enums.OrderStatus.ACCEPTED);
        orderDao.updateDriverForOrder(order.getId(), authService.getCurrentUser().getId());
        addButton.setDisable(true);
    }

}
