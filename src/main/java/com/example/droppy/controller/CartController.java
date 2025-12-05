package com.example.droppy.controller;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Locale;

public class CartController {
    private AuthService authService;
    private OrderDao orderDao;
    private Order currentOrder;
    private Runnable refreshCallback;

    public void init(AuthService authService) {
        this.authService = authService;
        this.orderDao = new HibernateOrderDao();
        this.currentOrder = orderDao.findByStatusAndUser(
                OrderStatus.IN_PREPARATION,
                authService.getCurrentUser()
        );

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        nameDemo.setText(authService.getCurrentUser().getName());
        surnameDemo.setText(authService.getCurrentUser().getSurname());

        paymentMethodCB.getItems().addAll(MethodOfPayment.values());
        paymentMethodCB.setValue(currentOrder.getPaymentMethod());

        paymentMethodCB.setOnAction(event -> {
            MethodOfPayment selectedMethod = paymentMethodCB.getValue();
            currentOrder.setPaymentMethod(selectedMethod);
            orderDao.updatePaymentMethod(currentOrder.getId(), selectedMethod);

        });


        String address =
                authService.getCurrentUser().getAddress().getStreet() + ", "
                        + authService.getCurrentUser().getAddress().getPostalCode() + " "
                        + authService.getCurrentUser().getAddress().getCity() + ", "
                        + authService.getCurrentUser().getAddress().getCountry();

        addressTextField.setText(address);
        PNTextField.setText(authService.getCurrentUser().getPhoneNumber());

        OTDemo.setText(String.valueOf(currentOrder.getTotalPrice()));
        totalDemo.setText(String.valueOf(currentOrder.getTotalPrice() + 0.5));

        refreshItems();
    }


    public void refreshItems() {
        OTDemo.setText(String.format("%.2f", currentOrder.getTotalPrice()));
        totalDemo.setText(String.format("%.2f", currentOrder.getTotalPrice() + 0.5));
        orderListContainer.getChildren().clear();

        for (OrderItem item : currentOrder.getOrderItems()) {
            if (item.getQuantity() <= 0) continue;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/ItemCartComponent.fxml"));
                Node node = loader.load();

                ItemCartController controller = loader.getController();
                controller.init(authService, currentOrder, item, this::refreshItems);

                orderListContainer.getChildren().add(node);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public Label paymentMethodLalbel;

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
    private ChoiceBox<MethodOfPayment> paymentMethodCB;


    @FXML
    void onOrderButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent rootPane = loader.load();

            HomeController controller = loader.getController();
            controller.init(authService);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }

        orderDao.updateOrderStatus(currentOrder.getId(), OrderStatus.PENDING);

    }

    @FXML
    void onProfileClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileView.fxml"));
            Parent rootPane = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

            ProfileController controller = loader.getController();
            controller.init(authService, stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText(){
        productsLabel.setText(I18n.get("product"));
        userInfoLabel.setText(I18n.get("userInfo"));
        addressLabel.setText(I18n.get("address"));
        PNLabel.setText(I18n.get("pNumber"));
        PriceLabel.setText(I18n.get("price"));
        OrderTotalLabel.setText(I18n.get("orderTotalPrice"));
        serviceFeeLabel.setText(I18n.get("serviceFee"));
        totalLabel.setText(I18n.get("total"));
        paymentMethodLalbel.setText(I18n.get("payMethod"));
        OrderButton.setText(I18n.get("order"));
    }
}
