package com.example.droppy.controller.product;

import com.example.droppy.controller.company.SaveCompanyController;
import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.repository.HibernateProductDao;
import com.example.droppy.repository.ProductDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import com.example.droppy.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.Locale;

public class SaveProductController {


    private AuthService authService;
    private Mode mode;
    private ProductDao productDao;
    private Company company;

    public void init(AuthService authService, Company company) {
        this.authService = authService;
        this.mode = mode;
        this.productDao = new HibernateProductDao(HibernateUtil.getSessionFactory());
        this.company = company;

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        if(deleteProductButton != null) {
            deleteProductButton.setVisible(mode == Mode.EDITING_PRODUCT);
            deleteProductButton.setManaged(mode == Mode.EDITING_PRODUCT);
        }
    }

    public enum Mode {
        ADDING_PRODUCT,
        EDITING_PRODUCT
    }

    @Setter
    private SaveCompanyController parentController;

    @FXML
    private Label EURLabel;

    @FXML
    private Button backtoCompanyButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private TextArea descProductTextField;

    @FXML
    private TextField nameProductTextField;

    @FXML
    private TextField priceProductTextField;

    @FXML
    private Label productLabel;

    @FXML
    private Button saveProductButton;

    @FXML
    void onBackToCompanyButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void onDeleteProductButtonClick(ActionEvent event) {

    }

    @FXML
    void onSaveProductButtonClick(ActionEvent event) {
        String name = nameProductTextField.getText().trim();
        String desc = descProductTextField.getText().trim();
        String priceText = priceProductTextField.getText().trim();

        if(name.isEmpty() || desc.isEmpty() || priceText.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").showAndWait();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid price format.").showAndWait();
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);
        product.setCompany(company);
        productDao.save(product);

        if (parentController != null) {
            parentController.addProductToList(product);
        }


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void updateText(){
        nameProductTextField.setPromptText(I18n.get("nameProduct"));
        descProductTextField.setPromptText(I18n.get("descProduct"));
        priceProductTextField.setPromptText(I18n.get("priceProduct"));
        deleteProductButton.setText(I18n.get("delete"));
        saveProductButton.setText(I18n.get("save"));
    }

}
