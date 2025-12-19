package com.example.droppy.controller.driver;

import com.example.droppy.controller.order.Order2ComponentController;
import com.example.droppy.controller.order.OrderComponentController;
import com.example.droppy.controller.auth.LoginController;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.DriverStatus;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.dao.OrderDao;
import com.example.droppy.repository.dao.UserDao;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DriverOrderController {

    private AuthService authService;
    private OrderDao orderDao;
    private Mode mode;
    private UserDao userDao;

    public enum Mode {
        AVAILABLE,
        ACCEPTED,
        DELIVERED
    }

    public  void init (AuthService authService, OrderDao orderDao ,Mode mode){
        this.authService = authService;
        this.mode = mode;
        this.orderDao = orderDao;
        this.userDao = authService.getUserDao();
        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        User user  = authService.getCurrentUser();
        if(user != null){
            nameDriverLabel.setText(user.getName());
            surnameDriverLabel.setText(user.getSurname());
        }

        if(AcOrderButton != null){
            AcOrderButton.setVisible(mode !=  Mode.ACCEPTED);
            AcOrderButton.setManaged(mode !=  Mode.ACCEPTED);
        }

        if(AvOrderButton != null){
            AvOrderButton.setVisible(mode !=  Mode.AVAILABLE);
            AvOrderButton.setManaged(mode !=  Mode.AVAILABLE);
        }

        if(DeOrderButton != null) {
            DeOrderButton.setVisible(mode != Mode.DELIVERED);
            DeOrderButton.setManaged(mode != Mode.DELIVERED);
        }

        driverStatusCB .getItems().setAll(DriverStatus.values());
        driverStatusCB.setValue(DriverStatus.ONLINE);
        userDao.updateStatus(user.getId(), DriverStatus.valueOf(driverStatusCB.getValue().toString()));
        driverStatusCB.getSelectionModel().selectedItemProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus != null) {
                userDao.updateStatus(user.getId(), newStatus);
            }
        });


        switch (mode) {
            case AVAILABLE -> availableOrderLabel.setText(I18n.get("avaOrder"));
            case ACCEPTED -> availableOrderLabel.setText(I18n.get("accOrder"));
            case DELIVERED -> availableOrderLabel.setText(I18n.get("deliOrder"));
        }

        List<Order> orders = switch (mode) {
            case AVAILABLE -> orderDao.findByStatus(OrderStatus.PENDING);
            case ACCEPTED -> orderDao.findByStatus(OrderStatus.ACCEPTED);
            case DELIVERED -> orderDao.findByStatus(OrderStatus.DELIVERED);
        };

        column1VBOX.getChildren().clear();

        System.out.println("Found orders: " + orders.size());
        for (Order order : orders) {
            if(order.getDriverId() != null) {
                if (!Objects.equals(order.getDriverId().getId(), authService.getCurrentUser().getId()) && order.getStatus() == OrderStatus.DELIVERED) {
                    continue;
                }
            }

            switch (mode) {
                case Mode.AVAILABLE -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/OrderComponent.fxml"));
                        Node orderComponent = loader.load();

                        OrderComponentController controller = loader.getController();
                        if (controller != null) {
                            controller.init(authService, order);
                        }

                        column1VBOX.getChildren().add(orderComponent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case Mode.ACCEPTED -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Order2Component.fxml"));
                        Node orderComponent = loader.load();

                        Order2ComponentController controller = loader.getController();
                        if (controller != null) {
                            controller.init(order, Order2ComponentController.Mode.ACCEPTED);
                        }

                        column1VBOX.getChildren().add(orderComponent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                case Mode.DELIVERED -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Order2Component.fxml"));
                        Node orderComponent = loader.load();

                        Order2ComponentController controller = loader.getController();
                        if (controller != null) {
                            controller.init(order, Order2ComponentController.Mode.DELIVERED);
                        }
                        column1VBOX.getChildren().add(orderComponent);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    private Button AcOrderButton;

    @FXML
    private Button AvOrderButton;

    @FXML
    private Button DeOrderButton;

    @FXML
    private Label availableOrderLabel;

    @FXML
    private VBox column1VBOX;

    @FXML
    private ChoiceBox<DriverStatus> driverStatusCB;

    @FXML
    private Button logOutButton;

    @FXML
    private Label nameDriverLabel;

    @FXML
    private Label surnameDriverLabel;

    @FXML
    void onAcOrderButtonClick(ActionEvent event) {
        if(mode == Mode.AVAILABLE){
            init(this.authService, this.orderDao, Mode.ACCEPTED);
        } else if (mode == Mode.DELIVERED) {
            init(this.authService, this.orderDao, Mode.ACCEPTED);
        }
    }

    @FXML
    void onAvOrderButtonClick(ActionEvent event) {
        if(mode == Mode.ACCEPTED){
            init(this.authService, this.orderDao, Mode.AVAILABLE);
        } else if (mode == Mode.DELIVERED) {
            init(this.authService, this.orderDao, Mode.AVAILABLE);
        }
    }

    @FXML
    void onDeOrderButtonClick(ActionEvent event) {
        if(mode == Mode.ACCEPTED){
            init(this.authService, this.orderDao, Mode.DELIVERED);
        } else if (mode == Mode.AVAILABLE) {
            init(this.authService, this.orderDao, Mode.DELIVERED);
        }
    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws IOException {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController loginController = loader.getController();
            if (this.authService != null) {
                userDao.updateStatus(authService.getCurrentUser().getId(), DriverStatus.OFFLINE);
                loginController.init(this.authService, () -> {});
            }

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }
    }

    private void updateText(){
        availableOrderLabel.setText(I18n.get("avaOrder"));
        AvOrderButton.setText(I18n.get("avaOrder"));
        AcOrderButton.setText(I18n.get("accOrder"));
        DeOrderButton.setText(I18n.get("deliOrder"));
        logOutButton.setText(I18n.get("log_out"));
    }
}
