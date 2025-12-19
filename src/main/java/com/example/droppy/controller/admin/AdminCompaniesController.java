package com.example.droppy.controller.admin;

import com.example.droppy.controller.auth.LoginController;
import com.example.droppy.controller.company.SaveCompanyController;
import com.example.droppy.domain.entity.Company;
import com.example.droppy.repository.dao.CompanyDao;
import com.example.droppy.repository.hibernate.HibernateCompanyDao;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AdminCompaniesController {

    private AuthService authService;
    private Set<Long> selectedCompanies;
    private CompanyDao companyDao;
    private Mode mode;

    public enum Mode {
        LIST_ALL,
        EDITING_COMPANIES
    }

    public void init(AuthService authService, Mode mode) {
        this.selectedCompanies = new HashSet<>();
        this.authService = authService;
        this.companyDao = new HibernateCompanyDao( HibernateUtil.getSessionFactory());
        this.mode = mode;

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        if(deleteCompanyButton != null) {
            deleteCompanyButton.setVisible(mode != Mode.LIST_ALL);
            deleteCompanyButton.setManaged(mode != Mode.LIST_ALL);
        }
        if(editCompanyButton != null) {
            editCompanyButton.setVisible(mode == Mode.LIST_ALL || mode == Mode.EDITING_COMPANIES);
            editCompanyButton.setManaged(mode == Mode.LIST_ALL || mode == Mode.EDITING_COMPANIES);
        }
        if(addCompanyButton != null) {
            addCompanyButton.setVisible(mode == Mode.LIST_ALL);
            addCompanyButton.setManaged(mode == Mode.LIST_ALL);
        }
        if(returnButton != null) {
            returnButton.setVisible(mode != Mode.LIST_ALL);
            returnButton.setManaged(mode != Mode.LIST_ALL);
        }
        if(switchToDriversButton != null) {
            switchToDriversButton.setVisible(mode == Mode.LIST_ALL);
            switchToDriversButton.setManaged(mode == Mode.LIST_ALL);
        }

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
    private Button editCompanyButton;

    @FXML
    private Button deleteCompanyButton;

    @FXML
    private Button returnButton;

    @FXML
    private ListView<Company> companiesListView;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToDriversButton;

    @FXML
    void addCompanyButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveCompany.fxml"));
            Parent rootPane = loader.load();

            SaveCompanyController controller = loader.getController();
            controller.init(authService, SaveCompanyController.Mode.ADDING_COMPANY, null);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy - Add Company");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to switch to adding mode: " + e.getMessage()).showAndWait();
        }
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
//        System.out.println("Selected company IDs: " + selectedCompanies);
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
        selectedCompanies.clear();
        loadCompanies();
    }

    @FXML
    void editCompanyButtonClick(ActionEvent event) {
        if(mode == Mode.LIST_ALL){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
                Parent rootPane = loader.load();

                AdminCompaniesController controller = loader.getController();
                controller.init(authService, Mode.EDITING_COMPANIES);

                Scene scene = new Scene(rootPane);
                stage.setScene(scene);
                stage.setTitle("Droppy - Edit Companies");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to switch to editing mode: " + e.getMessage()).showAndWait();
            }
        }

        if(mode == Mode.EDITING_COMPANIES){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveCompany.fxml"));
                Parent rootPane = loader.load();

                SaveCompanyController controller = loader.getController();
                controller.init(authService, SaveCompanyController.Mode.EDITING_COMPANY, companyDao.findById(selectedCompanies.iterator().next()));

                Scene scene = new Scene(rootPane);
                stage.setScene(scene);
                stage.setTitle("Droppy - Edit Company");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to switch to adding mode: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    void onDriversSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
        Parent rootPane = loader.load();

        AdminDriversController controller = loader.getController();
        controller.init(authService, AdminDriversController.Mode.LIST_ALL);

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

    @FXML
    void returnButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
            Parent rootPane = loader.load();

            AdminCompaniesController controller = loader.getController();
            controller.init(authService, Mode.LIST_ALL);

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy - Admin Companies");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to switch to list all mode: " + e.getMessage()).showAndWait();
        }
    }

    private void updateText(){
        addCompanyButton.setText(I18n.get("addCompany"));
        editCompanyButton.setText(I18n.get("editCompany"));
        switchToDriversButton.setText(I18n.get("drivers"));
        logOutButton.setText(I18n.get("log_out"));
        returnButton.setText(I18n.get("return"));
        deleteCompanyButton.setText(I18n.get("deleteCompanies"));
    }
}
