package com.example.droppy.controller;

import com.example.droppy.Navigator.Navigator;
import com.example.droppy.domain.entity.User;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.CartService;
import com.example.droppy.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    private AuthService authService;
    private Runnable onLoginSuccess;
    private CartService cartService;

    public void init(AuthService authService,Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
    }

    public void init1(AuthService authService, CartService cartService,Runnable onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
        this.cartService = cartService;
    }

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Text loginText;

    @FXML
    private Text noAccountText;

//    @FXML
//    private PasswordField passwordTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signinButton;
    @FXML
    void onLoginButtonClick(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if(email.isEmpty() || password.isEmpty()) {
            loginText.setText("Please enter both email and password.");
            return;
        }

        try {
            User currentUser = authService.login(email, password);

            Session.setLoggedUser(currentUser);
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }

            loginText.setText("Login Successful " + "Current User: " + email );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            switch (currentUser.getRole()){
                case CUSTOMER -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
                    Parent rootPane = loader.load();

                    HomeController controller = loader.getController();
                    controller.init(authService,cartService);

                    Scene scene = new Scene(rootPane);
                    stage.setScene(scene);
                }

                case ADMIN -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
                    Parent rootPane = loader.load();

                    AdminDriversController controller = loader.getController();
                    controller.init(authService, AdminDriversController.Mode.LIST_ALL);

                    Scene scene = new Scene(rootPane);
                    stage.setScene(scene);
                }

                case DRIVER -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/DriverAvailableOrderView.fxml"));
                    Parent rootPane = loader.load();

                    DriverAvailableOrderController controller = loader.getController();
                    controller.init(authService, new HibernateOrderDao(), DriverAvailableOrderController.Mode.AVAILABLE);

                    Scene scene = new Scene(rootPane);
                    stage.setScene(scene);
                }

                default -> {
                    throw new IllegalArgumentException("Unsupported role: " + currentUser.getRole());
                }


            }
            stage.setTitle("Droppy");
            stage.show();
        } catch (IllegalArgumentException e) {
            loginText.setText(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onSigninButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignInView.fxml"));
        Parent rootPane = loader.load();


        SignInController signInController = loader.getController();
        signInController.init(authService, () -> {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginCtrl = loginLoader.getController();
                loginCtrl.init1(authService, cartService, this.onLoginSuccess);
                var scene = new Scene(loginRoot);
                stage.setTitle("Droppy");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.show();
    }
}
