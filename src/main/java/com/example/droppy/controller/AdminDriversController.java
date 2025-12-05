package com.example.droppy.controller;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.UserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
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

import java.io.IOException;
import java.util.*;

public class AdminDriversController {

    public enum Mode {
        LIST_ALL,
        ADD_DRIVER_FROM_CUSTOMERS,
        DELETE_DRIVERS
    }

    private AuthService authService;
    private UserDao userDao;
    private Mode mode;

    @FXML
    private Button createNewDriver;

    @FXML
    private Button deleteDrivers;

    @FXML
    private Button addDriverButton;

    @FXML
    private Button editDriverButton;

    @FXML
    private Button returnToDefault;

    @FXML
    private ListView<User> driversListView;
    private Set<Long> selectedDrivers;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToCompaniesButton;


    public void init(AuthService authService, Mode mode) {
        this.mode = mode;
        this.selectedDrivers = new HashSet<>();
        this.authService = authService;
        this.userDao = authService.getUserDao();

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        if (addDriverButton != null) {
            addDriverButton.setVisible(mode != Mode.DELETE_DRIVERS);
            addDriverButton.setManaged(mode != Mode.DELETE_DRIVERS);
        }
        if (editDriverButton != null) {
            editDriverButton.setVisible(mode == Mode.LIST_ALL);
            editDriverButton.setManaged(mode == Mode.LIST_ALL);
        }
        if( createNewDriver != null) {
            createNewDriver.setVisible(mode == Mode.ADD_DRIVER_FROM_CUSTOMERS);
            createNewDriver.setManaged(mode == Mode.ADD_DRIVER_FROM_CUSTOMERS);
        }

        if(returnToDefault != null) {
            returnToDefault.setVisible(mode != Mode.LIST_ALL);
            returnToDefault.setManaged(mode != Mode.LIST_ALL);
        }

        if(switchToCompaniesButton != null) {
            switchToCompaniesButton.setVisible(mode == Mode.LIST_ALL);
            switchToCompaniesButton.setManaged(mode == Mode.LIST_ALL);
        }

        if(deleteDrivers != null) {
            deleteDrivers.setVisible(mode == Mode.DELETE_DRIVERS);
            deleteDrivers.setManaged(mode == Mode.DELETE_DRIVERS);
        }

        List<User> items = switch (mode) {
            case ADD_DRIVER_FROM_CUSTOMERS -> userDao.findByRole(Role.CUSTOMER);
            case DELETE_DRIVERS -> userDao.findByRole(Role.DRIVER);
            default -> userDao.findAll();
        };

        driversListView.setCellFactory(
                listView -> new javafx.scene.control.ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (empty || user == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            String mail = user.getEmail();
                            String newMail = "";

                            for (int i = 0; i< mail.length(); i++){
                                if(mail.charAt(i) == '@'){
                                    break;
                                }
                                newMail += mail.charAt(i);
                            }
                            newMail += ".driver@droppy.com";

                            if(userDao.findByEmail(newMail) != null && user.getRole() == Role.CUSTOMER) {
                                setText(user.getId() + ": " + user.getName() + " " + user.getSurname() + " - " + user.getEmail() + " " + user.getPhoneNumber() + " (" + user.getRole() + ")" +
                                        " ✅ (Already has driver account)");
                            }
                            else {
                                setText(user.getId() + ": " + user.getName() + " " + user.getSurname() + " - " + user.getEmail() + " " + user.getPhoneNumber() + " (" + user.getRole() + ")");
                            }
                            if (user.getId() != null && selectedDrivers.contains(user.getId())) {
                                setStyle("-fx-background-color: lightblue;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                }
        );

        driversListView.getItems().setAll(items);
    }

    void loadUsers() {
        List<User> allUsers = userDao.findAll();
        driversListView.getItems().setAll(allUsers);
    }

    @FXML
    void addDriverButtonAction(ActionEvent event) {
        if(mode != Mode.ADD_DRIVER_FROM_CUSTOMERS) {
            mode = Mode.ADD_DRIVER_FROM_CUSTOMERS;
            init(authService, mode);
            return;
        }

        if (selectedDrivers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one user.").show();
            return;
        }

        List<User> newDrivers = new ArrayList<>();
        int skippedCount = 0;

        for (Long idx : selectedDrivers) {
            User user = userDao.findById(idx);
            if (user == null) {
                skippedCount++;
                continue;
            }

            if (user.getRole() != Role.CUSTOMER) {
                skippedCount++;
                continue;
            }

            String mail = user.getEmail();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mail.length(); i++) {
                if (mail.charAt(i) == '@') break;
                sb.append(mail.charAt(i));
            }
            String newMail = sb.append(".driver@droppy.com").toString();

            if (userDao.findByEmail(newMail) != null) {
                skippedCount++;
                continue;
            }

            User copy = new User();
            copy.setName(user.getName());
            copy.setSurname(user.getSurname());
            copy.setEmail(newMail);
            copy.setPassword(user.getPassword());
            copy.setRole(Role.DRIVER);
            copy.setPhoneNumber(user.getPhoneNumber());
            copy.setCardNumber(user.getCardNumber());
            copy.setDeliveryMethod(user.getDeliveryMethod());
            copy.setDriverStatus(user.getDriverStatus());

            userDao.save(copy);
            newDrivers.add(copy);
        }

        if (!newDrivers.isEmpty()) {
            driversListView.getItems().addAll(newDrivers);
        }
        selectedDrivers.clear();

        String msg = "Selected users have been promoted to drivers.";
        if (skippedCount > 0) {
            msg += " Skipped " + skippedCount + " users (not customers, already drivers or missing).";
        }
        new Alert(Alert.AlertType.INFORMATION, msg).show();

        mode = Mode.LIST_ALL;
        init(authService, mode);
        selectedDrivers.clear();
        driversListView.getSelectionModel().clearSelection();

    }

    @FXML
    void driversListViewMouseClicked(MouseEvent event) {
        int idx = driversListView.getSelectionModel().getSelectedIndex();

        boolean isEmpty = driversListView.getSelectionModel().isEmpty();
        if(isEmpty) return;

        User clickedUser = driversListView.getItems().get(idx);

        String mail = clickedUser.getEmail();
        String newMail = "";

        for (int i = 0; i< mail.length(); i++){
            if(mail.charAt(i) == '@'){
                break;
            }
            newMail += mail.charAt(i);
        }
        newMail += ".driver@droppy.com";

        if ( userDao.findByEmail(newMail) != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "This user already has a driver account.");
            alert.show();
            return;
        }

        if(clickedUser.getRole() == Role.ADMIN) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You can only select users!");
            alert.show();
            return;
        }

        Long id = clickedUser.getId();
        if (selectedDrivers.contains(id)) {
            selectedDrivers.remove(id);
        } else {
            selectedDrivers.add(id);
        }

        driversListView.refresh();
        System.out.println("Selected driver IDs: " + selectedDrivers);
    }

    @FXML
    void deleteDriversButton(ActionEvent event) {
        if (selectedDrivers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one driver to delete.").show();
            return;
        }

        int deletedCount = 0;
        for (Long idx : selectedDrivers) {
            User user = userDao.findById(idx);
            if (user != null && user.getRole() == Role.DRIVER) {
                userDao.delete(idx);
                deletedCount++;
            }
        }

        if (deletedCount > 0) {
            loadUsers();
            new Alert(Alert.AlertType.INFORMATION, "Selected drivers have been deleted.").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "No valid drivers were selected for deletion.").show();
        }

        selectedDrivers.clear();
        mode = Mode.LIST_ALL;
        init(authService, mode);
        driversListView.getSelectionModel().clearSelection();
    }


    @FXML
    void editDriverButtonClick(ActionEvent event) {
        if(mode != Mode.DELETE_DRIVERS) {
            mode = Mode.DELETE_DRIVERS;
            init(authService, mode);
        }
    }

    @FXML
    void onCompaniesSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
        Parent rootPane = loader.load();

        AdminCompaniesController controller = loader.getController();
        controller.init(authService, AdminCompaniesController.Mode.LIST_ALL);

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy - Admin Companies");
        stage.show();
    }


    @FXML
    void createNewDriverButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SaveDriverView.fxml"));
        Parent rootPane = loader.load();

        SaveDriverController controller = loader.getController();

        controller.init(authService, () -> {
            try {
                FXMLLoader driversLoader = new FXMLLoader(getClass().getResource("/AdminDriversView.fxml"));
                Parent driversRootPane = driversLoader.load();

                AdminDriversController driversController = driversLoader.getController();
                driversController.init(authService, Mode.LIST_ALL);

                Scene driversScene = new Scene(driversRootPane);
                stage.setScene(driversScene);
                stage.setTitle("Droppy - Admin Drivers");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy - Create New Driver");
        stage.show();
    }

    @FXML
    void onLogOutButtonClicked(ActionEvent event) throws Exception {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            authService.logout();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController controller = loader.getController();
            controller.init(authService, () -> {});

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }
    }

    @FXML
    void returnToDefaultButton(ActionEvent event) {
        mode = Mode.LIST_ALL;
        init(authService, mode);
        selectedDrivers.clear();
        driversListView.getSelectionModel().clearSelection();
    }

    private void updateText(){
        addDriverButton.setText(I18n.get("addDriver"));
        editDriverButton.setText(I18n.get("editDriver"));
        switchToCompaniesButton.setText(I18n.get("companies"));
        logOutButton.setText(I18n.get("log_out"));
    }
}
