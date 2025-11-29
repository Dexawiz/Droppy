package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.repository.HibernateProductDao;
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

    private HibernateProductDao HProductDao = new HibernateProductDao();

    private AuthService authService;

    public void init(AuthService authService, Company company) {
        this.authService = authService;

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
            controller.init(product);
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
}
