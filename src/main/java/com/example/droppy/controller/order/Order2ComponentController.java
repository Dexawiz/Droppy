package com.example.droppy.controller.order;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.hibernate.HibernateOrderDao;
import com.example.droppy.repository.hibernate.HibernateOrderItemDao;
import com.example.droppy.repository.dao.OrderDao;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import com.example.droppy.util.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Locale;

public class Order2ComponentController {

    HibernateOrderItemDao orderItemDao = new HibernateOrderItemDao( HibernateUtil.getSessionFactory());
    private Mode mode;
    private OrderDao orderDao;
    public enum Mode{
        ACCEPTED,
        DELIVERED
    }

    public void init(Order order, Mode mode) {
        this.mode = mode;
        this.orderDao = new HibernateOrderDao(HibernateUtil.getSessionFactory());

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();


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
        driverNameDemo.setText(order.getDriverId().getName());
        driverSurnameDemo.setText(order.getDriverId().getSurname());


        statusOfDelivery.getItems().setAll(OrderStatus.values());

        if(mode == Mode.ACCEPTED){
            statusOfDelivery.setValue(OrderStatus.ACCEPTED);
            orderDao .updateOrderStatus(order.getId(), OrderStatus.ACCEPTED);
            statusOfDelivery.getSelectionModel().selectedItemProperty().addListener((obs, oldStatus, newStatus) -> {
                if (newStatus != null) {
                    orderDao.updateOrderStatus(order.getId(), newStatus);
                }

                if(newStatus == OrderStatus.DELIVERED){
                    orderDao.updateOrderStatus(order.getId(), newStatus);
                    statusOfDelivery.setDisable(true);
                }
            });
        } else if (mode == Mode.DELIVERED) {
            statusOfDelivery.setValue(OrderStatus.DELIVERED);
            statusOfDelivery.setDisable(true);
        }

    }

    @FXML
    private VBox mainPane;
    @FXML
    private Label CompanyLabel;

    @FXML
    private Label EURLabel;

    @FXML
    private Label addressCompanyDemo;

    @FXML
    private Label addressToDeliverDemo;

    @FXML
    private Label addressToDeliverLabel;

    @FXML
    private Label companyAddressLabel;

    @FXML
    private Label driverLabel;

    @FXML
    private Label driverNameDemo;

    @FXML
    private Label driverSurnameDemo;

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
    private ChoiceBox<OrderStatus> statusOfDelivery;

    private void updateText(){
        CompanyLabel.setText(I18n.get("product"));
        companyAddressLabel.setText(I18n.get("companyAddress"));
        addressToDeliverLabel.setText(I18n.get("addressToDeliver"));
        productToDeliverLabel.setText(I18n.get("productsToDeliver"));
        priceLabel.setText(I18n.get("price"));
        driverLabel.setText(I18n.get("driverName"));
    }

}
