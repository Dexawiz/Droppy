package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

public class SaveCompanyController {

    private AuthService authService;
    private Mode mode;
    private CompanyDao companyDao;

    public enum Mode {
        ADDING_COMPANY,
        EDITING_COMPANY
    }

    public void init(AuthService authService, Mode mode) {
        this.mode = mode;
        this.authService = authService;
        this.companyDao =  new HibernateCompanyDao();

        if(deleteCompanyButton != null) {
            deleteCompanyButton.setVisible(mode == Mode.EDITING_COMPANY);
            deleteCompanyButton.setManaged(mode == Mode.EDITING_COMPANY);
        }

        foodListView.setCellFactory(
            param -> new ListCell<>() {
                @Override
                protected void updateItem(Product item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName() + " - " + String.format("%.2f", item.getPrice()) + " EUR");
                    }
                }
            }
        );
    }

    @Setter
    private SaveCompanyController parentController;

    @FXML
    private Button addFoodButton;

    @FXML
    private Button backtoCompaniesButton;

    @FXML
    private ChoiceBox<Category> categoryCompanyChoiceBox;

    @FXML
    private ComboBox<Integer> closingHourCBox;

    @FXML
    private ComboBox<Integer> closingMinuteCBox;

    @FXML
    private TextField companyAddressTextField;

    @FXML
    private Label companyLabel;

    @FXML
    private TextField companyNameTextField;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private Button deleteFoodButton;

    @FXML
    private Label foodLabel;

    @FXML
    private ListView<Product> foodListView;

    @FXML
    private ComboBox<Integer> openingHourCBox;

    @FXML
    private ComboBox<Integer> openingMinuteCBox;

    @FXML
    private TextField phoneNumberCompanyTextField;

    @FXML
    private Button saveCompanyButton;

    @FXML
    void onAddButtonClick(ActionEvent event) {

        Company company = new Company();
        company.setName( companyNameTextField.getText());
        company.setAddress( companyAddressTextField.getText());
        company.setPhoneNumber( phoneNumberCompanyTextField.getText());
        company.setCategory( categoryCompanyChoiceBox.getValue());
        company.setWorkStart(LocalTime.parse(String.format("%02d:%02d", openingHourCBox.getValue(), openingMinuteCBox.getValue())));
        company.setWorkEnd(LocalTime.parse(String.format("%02d:%02d", closingHourCBox.getValue(), closingMinuteCBox.getValue())));
        companyDao.save(company);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveProduct.fxml"));
            Parent root = loader.load();

            SaveProductController controller = loader.getController();
            controller.init(authService, company);
            controller.setParentController(this);

            // Создаём новое окно для продукта
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Product");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Add Product window: " + e.getMessage()).showAndWait();
        }
    }

    public void addProductToList(Product product) {
        foodListView.getItems().add(product);
    }


    @FXML
    void onBackToCompaniesButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
            Parent rootPane = loader.load();

            AdminCompaniesController controller = loader.getController();
            controller.init(authService, AdminCompaniesController.Mode.LIST_ALL);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy - Admin Companies");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to switch to list all mode: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void onDeleteCompanyButtonClick(ActionEvent event) {

    }

    @FXML
    void onDeleteFoodButtonClick(ActionEvent event) {
        Product selectedProduct = foodListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            foodListView.getItems().remove(selectedProduct);
            // Here you would also delete the product from the database if needed
        } else {
            new Alert(Alert.AlertType.WARNING, "No product selected to delete.").showAndWait();
        }
    }

    @FXML
    void onSaveBCompanyButtonClick(ActionEvent event) {
        int openHour = (int) openingHourCBox.getValue();
        int openMinute = (int) openingMinuteCBox.getValue();
        int closeHour = closingHourCBox.getValue();
        int closeMinute = closingMinuteCBox.getValue();

        String openTime = String.format("%02d:%02d", openHour, openMinute);
        String closeTime = String.format("%02d:%02d", closeHour, closeMinute);

        System.out.println("Working time: " + openTime + " – " + closeTime);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
            Parent rootPane = loader.load();

            AdminCompaniesController controller = loader.getController();
            controller.init(authService, AdminCompaniesController.Mode.LIST_ALL);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy - Admin Companies");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to switch to list all mode: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void initialize() {
        for (int h = 0; h < 24; h++) {
            openingHourCBox.getItems().add(h);
            closingHourCBox.getItems().add(h);
        }

        for (int m = 0; m < 60; m += 15) {
            openingMinuteCBox.getItems().add(m);
            closingMinuteCBox.getItems().add(m);
        }
        openingHourCBox.setValue(9);
        openingMinuteCBox.setValue(0);
        closingHourCBox.setValue(18);
        closingMinuteCBox.setValue(0);

        List<Category> categories = Category.getAllCategories();
        if (categoryCompanyChoiceBox != null) {
            categoryCompanyChoiceBox.getItems().addAll(categories);
            categoryCompanyChoiceBox.setValue(Category.OTHER);
        }
    }

}
