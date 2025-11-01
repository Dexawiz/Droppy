package com.example.droppy.controller;

import com.example.droppy.domain.entity.User;
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
import java.util.List;

public class AdminDriversController {
    private AuthService authService;
    private UserDao userDao;

    @FXML
    private Button addDriverButton;

    @FXML
    private Button deleteDriverButton;

    @FXML
    private ListView<String> driversListView;
    private List<Integer> selectedDrivers;


    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button logOutButton;

    @FXML
    private Button switchToCompaniesButton;


    public void init(AuthService authService) {
        this.selectedDrivers  = new ArrayList<>();
        this.authService = authService;
        this.userDao = authService.getUserDao();
        driversListView.setCellFactory(
                listView -> new javafx.scene.control.ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        int index = getIndex();
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            if (selectedDrivers.contains(index)) {
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

    private void loadUsers() {
        List<User> users = userDao.findAll();
        driversListView.getItems().clear();

        for (User user : users) {
            driversListView.getItems().add( user.getId() + ": " + user.getName() + " " + user.getSurname() + "  -  " + user.getEmail() + user.getPhoneNumber() + " (" + user.getRole() + ")" );
        }
    }

    @FXML
    void addDriverButtonAction(ActionEvent event) {

    }

    @FXML
    void driversListViewMouseClicked(MouseEvent event) {
        int idx = driversListView.getSelectionModel().getSelectedIndex();

        boolean isEmpty = driversListView.getSelectionModel().isEmpty();
        if(isEmpty) return;
        if (this.selectedDrivers.contains(idx)) {
            this.selectedDrivers.remove(Integer.valueOf(idx));
        } else {
            this.selectedDrivers.add(idx);
        }

        driversListView.refresh();
        System.out.println(this.selectedDrivers);
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
        if (result.get() == javafx.scene.control.ButtonType.OK) {
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
