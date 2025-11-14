package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CompanyComponentController {

    @FXML
    private Label AddressLabel;

    @FXML
    private Label CTDemoLabel;

    @FXML
    private Label CosingTimeLabel;

    @FXML
    private Label OTDemoLabel;

    @FXML
    private Label adressCompanyDemoLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label nameOfCompanyLabel;

    @FXML
    private Button openCompanyButton;

    @FXML
    private Label openingTimeLabel;

    @FXML
    void onOpenCompanyButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CompanyView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) openCompanyButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            CompanyNameController controller = fxmlLoader.getController();
            CompanyDao companyDao = new HibernateCompanyDao();
            controller.init(companyDao.findByName(nameOfCompanyLabel.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Company company) {
        nameOfCompanyLabel.setText(company.getName());
        categoryLabel.setText(company.getCategory().name());
        adressCompanyDemoLabel.setText(company.getAddress());
        OTDemoLabel.setText(String.valueOf(company.getWorkStart()));
        CTDemoLabel.setText(String.valueOf(company.getWorkEnd()));
    }
}
