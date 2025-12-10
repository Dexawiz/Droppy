package com.example.droppy;

import com.example.droppy.controller.LoginController;
import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import com.example.droppy.repository.HibernateOrderDao;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.util.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        var userDao = new HibernateUserDao( HibernateUtil.getSessionFactory());
        var authService = new AuthService(userDao);

        // register default admin
        if (userDao.findByEmail("admin@droppy.com") == null) {
            authService.register(
                    "Admin",
                    "Root",
                    "admin@droppy.com",
                    "admin123",
                    "admin123",
                    Role.ADMIN
            );
        }

        var loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent rootPane = loader.load();

        // initialize controller with authService
        LoginController controller = loader.getController();
        controller.init(authService, () -> {});

        var scene = new Scene(rootPane);
        stage.setTitle("Droppy");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(500);

        stage.setOnCloseRequest(event -> {
            var orderDao = new HibernateOrderDao(HibernateUtil.getSessionFactory());
            List <Order> ordersForDeleting = orderDao.findByStatus(com.example.droppy.domain.enums.OrderStatus.IN_PREPARATION);
            for (Order order : ordersForDeleting) {
                orderDao.delete(order.getId());
            }
            var companyDao = new HibernateCompanyDao(HibernateUtil.getSessionFactory());
            List<Company> companies = companyDao .findAll();
            for (Company company : companies) {
                if (company.getName() == null || company.getName().isEmpty()) {
                    companyDao.delete(company.getId());
                    }
                }
        });


        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
