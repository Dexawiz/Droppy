package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
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
import javafx.scene.control.ListCell;
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
        // инициализируем DAO для компаний
        this.companyDao = new HibernateCompanyDao();

        // защитная инициализация ListView: установка cellFactory и фильтра кликов
        companiesListView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            var selectionModel = companiesListView.getSelectionModel();
            if (selectionModel == null) {
                return;
            }
            int idx = selectionModel.getSelectedIndex();
            if (idx < 0) {
                selectionModel.clearSelection();
            }
        });

        companiesListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Company company, boolean empty) {
                super.updateItem(company, empty);
                if (empty || company == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(company.getId() + ": " + company.getName() + " (" + company.getCategory() + ")" +
                            " - " + company.getAddress() + ", " + company.getPhoneNumber());
                    if (company.getId() != null && selectedCompanies.contains(company.getId())) {
                        setStyle("-fx-background-color: lightblue;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        loadCompanies();

    }
    @FXML
    private Button addCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private ListView<Company> companiesListView;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToDriversButton;

    @FXML
    void addCompanyButtonAction(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "TODO").show();
    }

    @FXML
    void companiesListViewMouseClicked(MouseEvent event) {
        boolean isEmpty = companiesListView.getSelectionModel().isEmpty();
        if (isEmpty) return;

        int idx = companiesListView.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;

        Company clicked = companiesListView.getItems().get(idx);
        if (clicked == null || clicked.getId() == null) return;

        Long id = clicked.getId();
        if (selectedCompanies.contains(id)) {
            selectedCompanies.remove(id);
        } else {
            selectedCompanies.add(id);
        }

        companiesListView.refresh();
        System.out.println("Selected company IDs: " + selectedCompanies);
    }

    void loadCompanies() {
        if (companyDao == null) {
            new Alert(Alert.AlertType.ERROR, "Company DAO is not initialized").showAndWait();
            return;
        }
        List<Company> all = companyDao.findAll();
        companiesListView.getItems().setAll(all);
    }

    @FXML
    void deleteCompanyButtonClick(ActionEvent event) {
        if (selectedCompanies == null || selectedCompanies.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one company to delete.").show();
            return;
        }

        for (Long id : selectedCompanies) {
            companyDao.delete(id);
        }

        loadCompanies();
        selectedCompanies.clear();
        companiesListView.getSelectionModel().clearSelection();
        new Alert(Alert.AlertType.INFORMATION, "Selected companies have been deleted.").show();
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
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
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
