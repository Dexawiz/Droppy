package com.example.droppy.controller.company;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
import com.example.droppy.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;

public class CompanyComponentController {

    private AuthService authService;

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
    private VBox mainPane;

    public void init (AuthService authService, Company company) {
        this.authService = authService;

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        nameOfCompanyLabel.setText(company.getName());
        categoryLabel.setText(company.getCategory().getTranslated());
        adressCompanyDemoLabel.setText(company.getAddress());
        OTDemoLabel.setText(String.valueOf(company.getWorkStart()));
        CTDemoLabel.setText(String.valueOf(company.getWorkEnd()));

        URL css = getClass().getResource("/styles/CompanyComponentStyl.css");
        if(css != null) {
            mainPane.getStylesheets().add(css.toExternalForm());
        }
    }

    @FXML
    void onOpenCompanyButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CompanyView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) openCompanyButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            CompanyViewController controller = fxmlLoader.getController();
            CompanyDao companyDao = new HibernateCompanyDao(HibernateUtil.getSessionFactory());
            controller.init(authService, companyDao.findByName(nameOfCompanyLabel.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText(){
        CosingTimeLabel.setText(I18n.get("cTimeShort"));
        openingTimeLabel.setText(I18n.get("oTimeShort"));
        AddressLabel.setText(I18n.get("address"));
    }
}
