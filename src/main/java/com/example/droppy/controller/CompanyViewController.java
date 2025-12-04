package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.HibernateProductDao;
import com.example.droppy.repository.OrderDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class CompanyViewController {
    @FXML
    private FontIcon shoppingCartIcon;

    @FXML
    private Label AddressDemo;

    @FXML
    private Label CTDemoLabel;

    @FXML
    private Label CTLabel;

    @FXML
    private Label ItemsLabel;

    @FXML
    private Label OTDemoLabel;

    @FXML
    private Label OTLabel;

    @FXML
    private Label PNDemo;

    @FXML
    private Label PNLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label categoryNameLabel;

    @FXML
    private VBox column1VBOX;

    @FXML
    private VBox column2VBOX;

    @FXML
    private VBox column3VBOX;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private HBox header;

    @FXML
    private ScrollPane itemScrollPane;

    @FXML
    private Label restaurantNameLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button returnButton;

    @FXML
    private Button cartButton;

    private HibernateProductDao HProductDao = new HibernateProductDao();

    private AuthService authService;
    private Order currentOrder;

    public void init(AuthService authService, Company company) {
        this.authService = authService;
        OrderDao orderDao = new HibernateOrderDao();

        Order existingOrder = orderDao.findByStatusAndUser(OrderStatus.IN_PREPARATION, authService.getCurrentUser());

        if (existingOrder != null) {
            this.currentOrder = existingOrder;
        } else {
            this.currentOrder = new Order();
            currentOrder.setCustomerId(authService.getCurrentUser());
            currentOrder.setCompanyId(company);
            currentOrder.setStatus(OrderStatus.IN_PREPARATION);
            currentOrder.setDeliveryFromAddress(company.getAddress());
            String address =
                    authService.getCurrentUser().getAddress().getStreet() + ", "
                            + authService.getCurrentUser().getAddress().getPostalCode() + " "
                            + authService.getCurrentUser().getAddress().getCity() + ", "
                            + authService.getCurrentUser().getAddress().getCountry();
            currentOrder.setDeliveryToAddress(address);
            currentOrder.setTotalPrice(0.);
            currentOrder.setOrderCreatedTime( java.time.LocalDateTime.now());
            currentOrder.setPaymentMethod(MethodOfPayment.ONLINE);
            if (currentOrder.getOrderItems() == null) {
                currentOrder.setOrderItems(new ArrayList<>());
            }
            orderDao.save(currentOrder);
        }

        restaurantNameLabel.setText(company.getName());
        categoryNameLabel.setText(company.getCategory().name());
        OTDemoLabel.setText(company.getWorkStart().toString());
        CTDemoLabel.setText(company.getWorkEnd().toString());
        AddressDemo.setText(company.getAddress());
        PNDemo.setText(company.getPhoneNumber());

        List<Product> products = HProductDao.findByCompanyId(company.getId());
        int index = 0;
        for (Product product : products) {
            Node productNode = loadItems(product);
            if (productNode == null) continue;

            if (index % 3 == 0) {
                column1VBOX.getChildren().add(productNode);
            } else if (index % 3 == 1) {
                column2VBOX.getChildren().add(productNode);
            } else {
                column3VBOX.getChildren().add(productNode);
            }
            index++;
        }
    }

    private Node loadItems(Product product) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/components/FoodItemComponent.fxml"));
            Node node = fxmlLoader.load();
            FoodItemController controller = fxmlLoader.getController();
            controller.init(authService, product, currentOrder);
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void returnButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent rootPane = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

            HomeController controller = loader.getController();
            controller.init(authService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCartButtonClick(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CheckoutView.fxml"));
            Parent rootPane = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

            CheckoutController controller = loader.getController();
            controller.init(authService);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
