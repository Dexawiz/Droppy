package com.example.droppy.controller;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.UserDao;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminDriversController {
    private AuthService authService;
    private UserDao userDao;

    @FXML
    private Button addDriverButton;

    @FXML
    private Button deleteDriverButton;

    @FXML
    private ListView<User> driversListView;
    private Set<Long> selectedDrivers;


    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToCompaniesButton;


    public void init(AuthService authService) {
        this.selectedDrivers  = new HashSet<>();
        this.authService = authService;
        this.userDao = authService.getUserDao();

        driversListView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            var selectionModel = driversListView.getSelectionModel();
            if (selectionModel == null) {
                return;
            }
            int idx = selectionModel.getSelectedIndex();
            if (idx < 0) {
                selectionModel.clearSelection();
            }
        });

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
        loadUsers();
    }

    void loadUsers() {
        List<User> allUsers = userDao.findAll();
        driversListView.getItems().setAll(allUsers);
    }

    @FXML
    void addDriverButtonAction(ActionEvent event) {
        List<User> newDrivers = new ArrayList<>();

        if (selectedDrivers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one user.").show();
            return;
        }

        for (Long idx : selectedDrivers){
            User user = userDao.findById( idx);
            if (user != null) {
                String mail = user.getEmail();
                String newMail = "";

                for (int i = 0; i< mail.length(); i++){
                    if(mail.charAt(i) == '@'){
                        break;
                    }
                    newMail += mail.charAt(i);
                }
                newMail += ".driver@droppy.com";

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

            driversListView.getItems().addAll(newDrivers);
        }

        driversListView.refresh();
        selectedDrivers.clear();
        driversListView.getSelectionModel().clearSelection();
        new Alert(Alert.AlertType.INFORMATION, "Selected users have been promoted to drivers.").show();
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

        if ( userDao.findByEmail(newMail) != null && clickedUser.getRole() == Role.CUSTOMER) {
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
    void deleteDriverButtonClick(ActionEvent event) {
        if (selectedDrivers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one user to delete.").show();
            return;
        }

        for (Long idx : selectedDrivers){
            User user = userDao.findById( idx);
            if (user != null) {
                userDao.delete(idx);
            }
        }

        loadUsers();
        selectedDrivers.clear();
        driversListView.getSelectionModel().clearSelection();
        new Alert(Alert.AlertType.INFORMATION, "Selected users have been deleted.").show();
    }

    @FXML
    void onCompaniesSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
        Parent rootPane = loader.load();

        AdminCompaniesController controller = loader.getController();
        controller.init(authService);

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy - Admin Companies");
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
}
