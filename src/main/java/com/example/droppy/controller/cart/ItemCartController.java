package com.example.droppy.controller.cart;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.repository.hibernate.HibernateOrderDao;
import com.example.droppy.repository.dao.OrderDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import com.example.droppy.service.ThemeStyles;
import com.example.droppy.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Locale;

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

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        this.orderDao = new HibernateOrderDao( HibernateUtil.getSessionFactory());
        ItemNameLabel.setText(item.getProduct().getName());
        priceDemoLabel.setText(String.valueOf(item.getPricePerItem()));
        numberOfItemsLabel.setText(String.valueOf(item.getQuantity()));
        decriptionText.setText(item.getProduct().getDescription());

        URL css = getClass().getResource("/styles/BGColorComponents.css");
        if(css != null) {
            mainPane.getStylesheets().add(css.toExternalForm());
        }
        changeBG(ThemeStyles.isDarkMode());
    }

    @FXML
    private Label ItemNameLabel;

    @FXML
    private Text decriptionText;

    @FXML
    private Label numberOfItemsLabel;

    @FXML
    private Label priceDemoLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private VBox mainPane;

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

    private void updateText(){
        priceLabel.setText(I18n.get("price"));
    }

    private void changeBG(boolean isDarkMode){
        mainPane.getStyleClass().remove("dark");
        if(isDarkMode){
            mainPane.getStyleClass().add("dark");
        }
    }

}
