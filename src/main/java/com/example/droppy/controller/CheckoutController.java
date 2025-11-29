package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;

public class CheckoutController {

    private AuthService authService;
    private OrderDao orderDao;

    public void init(AuthService authService) {
        this.authService = authService;
        this.orderDao = new HibernateOrderDao();

        nameDemo.setText(authService.getCurrentUser().getName());
        surnameDemo.setText(authService.getCurrentUser().getSurname());
        String address =
                authService.getCurrentUser().getAddress().getStreet() + ", "
               + authService.getCurrentUser().getAddress().getPostalCode() + " "
                + authService.getCurrentUser().getAddress().getCity() + ", "
                + authService.getCurrentUser().getAddress().getCountry();
        addressTextField.setText(address);
        PNTextField.setText(authService.getCurrentUser().getPhoneNumber());

        Order currentOrder = orderDao.findByStatusAndUser( OrderStatus.IN_PREPARATION, authService.getCurrentUser());
        OTDemo.setText(String.valueOf(currentOrder.getTotalPrice()));
        totalDemo.setText(String.valueOf(currentOrder.getTotalPrice() + 0.5));

        orderListContainer.getChildren().clear();

        for (OrderItem item : currentOrder.getOrderItems()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/ItemCheckoutComponent.fxml"));
                Node node = loader.load();

                ItemCheckoutController controller = loader.getController();
                controller.init(authService, currentOrder, item);

                orderListContainer.getChildren().add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private VBox orderListContainer;

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
