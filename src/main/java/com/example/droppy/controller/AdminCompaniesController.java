package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.User;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminCompaniesController {

    private AuthService authService;
    private Set<Long> selectedCompanies;
    private CompanyDao companyDao;



    public void init(AuthService authService) {
        this.selectedCompanies = new HashSet<>();
        this.authService = authService;

    }
    @FXML
    private Button addCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private ListView<?> companiesListView;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToDriversButton;

    @FXML
    void addCompanyButtonAction(ActionEvent event) {

    }

    @FXML
    void companiesListViewMouseClicked(MouseEvent event) {

    }

    void loadCompanies() {

    }

    @FXML
    void deleteCompanyButtonClick(ActionEvent event) {

    }

    @FXML
    void onDriversSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
        Parent rootPane = loader.load();

        AdminDriversController controller = loader.getController();
        controller.init(authService);

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy - Admin Drivers");
        stage.show();
    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws Exception {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();
        if ((result.get() == javafx.scene.control.ButtonType.OK)) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController loginController = loader.getController();
            if (this.authService != null) {
                loginController.init(this.authService, () -> {});
            }

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }
    }
}
