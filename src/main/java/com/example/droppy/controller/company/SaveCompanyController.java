package com.example.droppy.controller.company;

import com.example.droppy.controller.product.SaveProductController;
import com.example.droppy.controller.admin.AdminCompaniesController;
import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.repository.dao.CompanyDao;
import com.example.droppy.repository.dao.ProductDao;
import com.example.droppy.repository.hibernate.HibernateCompanyDao;
import com.example.droppy.repository.hibernate.HibernateProductDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import com.example.droppy.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SaveCompanyController {

    private AuthService authService;
    private Mode mode;
    private CompanyDao companyDao;
    private Company company;

    public enum Mode {
        ADDING_COMPANY,
        EDITING_COMPANY
    }

    public void init(AuthService authService, Mode mode, Company company){
        this.mode = mode;
        this.authService = authService;
        this.companyDao =  new HibernateCompanyDao(HibernateUtil.getSessionFactory());
        this.company = company;

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        if (mode == Mode.EDITING_COMPANY && company != null) {
            fillForm(company);
        }
        
        if(deleteCompanyButton != null) {
            deleteCompanyButton.setVisible(mode == Mode.EDITING_COMPANY);
            deleteCompanyButton.setManaged(mode == Mode.EDITING_COMPANY);
        }

        productListView.setCellFactory(
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

    private void fillForm(Company company) {
        companyNameTextField.setText(company.getName());
        companyAddressTextField.setText(company.getAddress());
        phoneNumberCompanyTextField.setText(company.getPhoneNumber());
        categoryCompanyChoiceBox.setValue(company.getCategory());
        openingHourCBox.setValue(company.getWorkStart().getHour());
        openingMinuteCBox.setValue(company.getWorkStart().getMinute());
        closingHourCBox.setValue(company.getWorkEnd().getHour());
        closingMinuteCBox.setValue(company.getWorkEnd().getMinute());
        ProductDao productDao = new HibernateProductDao(HibernateUtil.getSessionFactory());
        List<Product> products = productDao.findByCompanyId(company.getId());
        productListView.getItems().addAll(products);
    }

    @Setter
    private SaveCompanyController parentController;

    @FXML
    private Button addProductButton;

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
    private Button deleteProductButton;

    @FXML
    private Label productLabel;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private ComboBox<Integer> openingHourCBox;

    @FXML
    private ComboBox<Integer> openingMinuteCBox;

    @FXML
    private TextField phoneNumberCompanyTextField;

    @FXML
    private Button saveCompanyButton;

    @FXML
    void onAddProductButtonClick(ActionEvent event) {

        if(company == null) {
            company  = new Company();
            company.setName( companyNameTextField.getText());
            company.setAddress( companyAddressTextField.getText());
            company.setPhoneNumber( phoneNumberCompanyTextField.getText());
            company.setCategory( categoryCompanyChoiceBox.getValue());
            company.setWorkStart(LocalTime.parse(String.format("%02d:%02d", openingHourCBox.getValue(), openingMinuteCBox.getValue())));
            company.setWorkEnd(LocalTime.parse(String.format("%02d:%02d", closingHourCBox.getValue(), closingMinuteCBox.getValue())));
            companyDao.save(company);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveProduct.fxml"));
            Parent root = loader.load();

            SaveProductController controller = loader.getController();
            controller.init(authService, company);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Product");
            stage.setMinWidth(500);
            stage.setMinHeight(500);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Add Product window: " + e.getMessage()).showAndWait();
        }
    }

    public void addProductToList(Product product) {
        productListView.getItems().add(product);
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
            stage.setMinWidth(500);
            stage.setMinHeight(500);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to switch to list all mode: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    void onDeleteCompanyButtonClick(ActionEvent event) {
        if (company != null) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete company?");
            var result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                companyDao.delete(company.getId());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
                    Parent rootPane = loader.load();

                    AdminCompaniesController controller = loader.getController();
                    controller.init(authService, AdminCompaniesController.Mode.LIST_ALL);

                    Scene scene = new Scene(rootPane);
                    stage.setScene(scene);
                    stage.setTitle("Droppy - Admin Companies");
                    stage.setMinWidth(500);
                    stage.setMinHeight(500);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to switch to list all mode: " + e.getMessage()).showAndWait();
                }
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No company to delete.").showAndWait();
        }
    }

    @FXML
    void onDeleteProductButtonClick(ActionEvent event) {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productListView.getItems().remove(selectedProduct);
            ProductDao productDao = new HibernateProductDao(HibernateUtil.getSessionFactory());
            productDao.delete(selectedProduct.getId());
        } else {
            new Alert(Alert.AlertType.WARNING, "No product selected to delete.").showAndWait();
        }
    }

    @FXML
    void onSaveBCompanyButtonClick(ActionEvent event) {

        company.setName(companyNameTextField.getText());
        company.setAddress(companyAddressTextField.getText());
        company.setPhoneNumber(phoneNumberCompanyTextField.getText());
        company.setCategory(categoryCompanyChoiceBox.getValue());

        company.setWorkStart(
                LocalTime.of(openingHourCBox.getValue(), openingMinuteCBox.getValue())
        );
        company.setWorkEnd(
                LocalTime.of(closingHourCBox.getValue(), closingMinuteCBox.getValue())
        );

        if (company.getId() == null) {
            companyDao.save(company);
        } else {
            companyDao.update(company);
        }


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
            Parent rootPane = loader.load();

            AdminCompaniesController controller = loader.getController();
            controller.init(authService, AdminCompaniesController.Mode.LIST_ALL);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy - Admin Companies");
            stage.setMinWidth(500);
            stage.setMinHeight(500);
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

        categoryCompanyChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getTranslated() : "";
            }

            @Override
            public Category fromString(String string) {
                return Arrays.stream(Category.values())
                        .filter(c -> c.getTranslated().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void updateText(){
        companyLabel.setText(I18n.get("company"));
        companyNameTextField.setPromptText(I18n.get("companyName"));
        companyAddressTextField.setPromptText(I18n.get("address"));
        phoneNumberCompanyTextField.setPromptText(I18n.get("phone_number"));
        openingHourCBox.setPromptText(I18n.get("openingHour"));
        openingMinuteCBox.setPromptText(I18n.get("openingMinute"));
        closingHourCBox.setPromptText(I18n.get("closingHour"));
        closingMinuteCBox.setPromptText(I18n.get("closingMinute"));
        productLabel.setText(I18n.get("product"));
        deleteCompanyButton.setText(I18n.get("delete"));
        saveCompanyButton.setText(I18n.get("save"));
    }
}
