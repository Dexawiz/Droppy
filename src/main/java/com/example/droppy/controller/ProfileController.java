package com.example.droppy.controller;

import com.example.droppy.domain.enums.Language;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import com.example.droppy.domain.entity.User;
import javafx.stage.Stage;

public class ProfileController {

    private AuthService authService;
    private Runnable onLoginSuccess;
    private Stage mainStage;

    public void init(AuthService authService, Stage mainStage) {
        this.authService = authService;
        this.mainStage = mainStage;
    }

    @FXML
    private Button addCreditDebitCardButton;

    @FXML
    private Circle avatarSmall;

    @FXML
    private Circle bigAvatar;

    @FXML
    private Label creditDebitCardLabel;

    @FXML
    private Text creditDebitCardText;

    @FXML
    private Button deleteProfileButton;

    @FXML
    private Label deleteProfileLabel;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Button editCreditDebitCardButton;

    @FXML
    private Button editProfileButton;

    @FXML
    private Label emailText;

    @FXML
    private HBox header;

    @FXML
    private Button logOutButton;

    @FXML
    private Label logOutLabel;

    @FXML
    private Text nameText;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label profileLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label settingLabel;

    @FXML
    private Text surnameText;

    @FXML
    private Text userEmailDemo;

    @FXML
    private Text userPhoneDemo;

    @FXML
    private Button backButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ChoiceBox<Language> languageCB;

    private boolean isEditing = false;

    @FXML
    void onAddCreditDebitCardButtonClick(ActionEvent event) {

    }

    @FXML
    void onDeleteProfileButtonClick(ActionEvent event) throws Exception {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete your profile?");
        var result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            User loggedUser = Session.getLoggedUser();
            if (loggedUser != null) {
                HibernateUserDao userDao = new HibernateUserDao();
                userDao.delete(loggedUser.getId());
                Session.logout();
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController loginController = loader.getController();
            if (this.authService != null) {
                loginController.init(this.authService, () -> {
                });
            }

            Scene scene = new Scene(rootPane);
            stage.setScene(scene);
            stage.setTitle("Droppy");
            stage.show();
        }
    }

    @FXML
    void onEditCreditDebitCardButtonClick(ActionEvent event) {

    }

    @FXML
    void onEditProfileButtonClick(ActionEvent event) {
        if(!isEditing){
            isEditing = true;
            editProfileButton.setText("Save");

            //ziskanie udajov
            nameField.setText(nameText.getText());
            surnameField.setText(surnameText.getText());
            phoneField.setText(userPhoneDemo.getText());

            //skrytie labels
            nameText.setVisible(false);
            nameText.setManaged(false);
            surnameText.setVisible(false);
            surnameText.setManaged(false);
            userPhoneDemo.setVisible(false);
            userPhoneDemo.setManaged(false);

            //ukazanie text fields
            nameField.setVisible(true);
            nameField.setManaged(true);
            surnameField.setVisible(true);
            surnameField.setManaged(true);
            phoneField.setVisible(true);
            phoneField.setManaged(true);
        }else{
            isEditing = false;
            editProfileButton.setText("Edit");

            User user = Session.getLoggedUser();
            if (user != null) {
                user.setName(nameField.getText());
                user.setSurname(surnameField.getText());
                user.setPhoneNumber(phoneField.getText());

                HibernateUserDao userDao = new HibernateUserDao();
                userDao.save(user);

                nameText.setText(user.getName());
                surnameText.setText(user.getSurname());
                userPhoneDemo.setText(user.getPhoneNumber());
            }

            nameText.setVisible(true);
            nameText.setManaged(true);
            surnameText.setVisible(true);
            surnameText.setManaged(true);
            userPhoneDemo.setVisible(true);
            userPhoneDemo.setManaged(true);

            nameField.setVisible(false);
            nameField.setManaged(false);
            surnameField.setVisible(false);
            surnameField.setManaged(false);
            phoneField.setVisible(false);
            phoneField.setManaged(false);
        }

    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeView.fxml"));
            Parent rootPane = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(rootPane);
            stage.setScene(scene);

            HomeController controller = loader.getController();
            controller.init(this.authService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    void onLogOutButtonClick(ActionEvent event) throws Exception {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?");
        var result = alert.showAndWait();

        if ((result.get() == javafx.scene.control.ButtonType.OK)) {
            authService.logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent rootPane = loader.load();

            LoginController loginController = loader.getController();
            if (this.authService != null) {
                loginController.init(this.authService, () -> {});
            }

            Scene scene = new Scene(rootPane);
            mainStage.setScene(scene);
            mainStage.setTitle("Droppy");
            mainStage.show();
        }
    }

    @FXML
    private void initialize() {
        languageCB.getItems().setAll(Language.values());
        languageCB.setValue(Session.getCurrentLanguage());

        languageCB.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Session.setCurrentLanguage(newVal);
            }
        });

        User user = Session.getLoggedUser();
        if (user != null) {
            nameText.setText(user.getName());
            surnameText.setText(user.getSurname());
            userEmailDemo.setText(user.getEmail());
            if (user.getPhoneNumber() != null) {
                userPhoneDemo.setText(user.getPhoneNumber());
            }
        }
    }



}
