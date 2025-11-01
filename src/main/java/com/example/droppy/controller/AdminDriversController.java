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
                            setText(user.getId() + ": " + user.getName() + " " + user.getSurname() + " - " + user.getEmail() + " " + user.getPhoneNumber() + " (" + user.getRole() + ")");
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

    }

    @FXML
    void driversListViewMouseClicked(MouseEvent event) {
        int idx = driversListView.getSelectionModel().getSelectedIndex();

        boolean isEmpty = driversListView.getSelectionModel().isEmpty();
        if(isEmpty) return;

        User clickedUser = driversListView.getItems().get(idx);
        if(clickedUser.getRole() != Role.CUSTOMER) {
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

    }

    @FXML
    void onCompaniesSwitchButtonClicked(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminCompaniesView.fxml"));
        Parent rootPane = loader.load();

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Droppy");
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
