package com.example.droppy.controller;

import com.example.droppy.domain.enums.Language;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
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

import java.util.Locale;

public class ProfileController {

    private AuthService authService;
    private Stage mainStage;

    public void init(AuthService authService, Stage mainStage) {
        this.authService = authService;
        this.mainStage = mainStage;
    }

    public void init(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    private Circle avatarSmall;

    @FXML
    private Circle bigAvatar;

    @FXML
    private Label creditDebitCardLabel;

    @FXML
    private Button deleteProfileButton;

    @FXML
    private Label deleteProfileLabel;

    @FXML
    private Label droppyTextLogo;

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
    private Label settingLabel;

    @FXML
    private Text surnameText;

    @FXML
    private Text userEmailDemo;

    @FXML
    private Text userPhoneDemo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    //pre kartu
    @FXML
    private TextField cardNumberTF;

    @FXML
    private TextField monthTF;

    @FXML
    private TextField yearTF;

    @FXML
    private PasswordField CCVTF;

    @FXML
    private Label noCardLabel;

    @FXML
    private Button addEditCardButton;

    @FXML
    private Button deleteCardButton;

    @FXML
    private Label languageLabel;


    @FXML
    private ChoiceBox<Language> languageCB1;

    private boolean isEditing = false;

    private boolean isAddingEditingCard = false;

    @FXML
    void onAddEditCardButtonCLick(ActionEvent event) {
        User user = Session.getLoggedUser();

        if (!isAddingEditingCard) {
            isAddingEditingCard = true;
            addEditCardButton.setText(I18n.get("save"));

            if (user != null && user.getCardNumber() != null) {
                String[] parts = user.getCardNumber().split("/");
                if (parts.length == 4) {
                    cardNumberTF.setText(parts[0]);
                    monthTF.setText(parts[1]);
                    yearTF.setText(parts[2]);
                    CCVTF.setText(parts[3]);
                }
            } else {
                cardNumberTF.clear();
                monthTF.clear();
                yearTF.clear();
                CCVTF.clear();
            }


            cardNumberTF.setVisible(true);
            cardNumberTF.setManaged(true);
            monthTF.setVisible(true);
            monthTF.setManaged(true);
            yearTF.setVisible(true);
            yearTF.setManaged(true);
            CCVTF.setVisible(true);
            CCVTF.setManaged(true);

            // skry label
            noCardLabel.setVisible(false);
            noCardLabel.setManaged(false);
        } else {
            // uloženie do DB
            String cardNumber = cardNumberTF.getText().trim();
            String month = monthTF.getText().trim();
            String year = yearTF.getText().trim();
            String ccv = CCVTF.getText().trim();

            if (!cardNumber.isEmpty() && !month.isEmpty() && !year.isEmpty()) {
                String card = cardNumber + "/" + month + "/" + year + "/" + ccv;

                //pridanie do db
                if (user != null) {
                    user.setCardNumber(card);
                    HibernateUserDao userDao = new HibernateUserDao();
                    userDao.save(user);
                }
                loadUserData();
                // maskovanie čísla karty
                noCardLabel.setText("****************" + "/" + "**" + "/" + "**");
                noCardLabel.setVisible(true);
                noCardLabel.setManaged(true);

                // skry text fields
                cardNumberTF.setVisible(false);
                cardNumberTF.setManaged(false);
                monthTF.setVisible(false);
                monthTF.setManaged(false);
                yearTF.setVisible(false);
                yearTF.setManaged(false);
                CCVTF.setVisible(false);
                CCVTF.setManaged(false);

                addEditCardButton.setText("Edit");
                isAddingEditingCard = false;
                loadUserData();

            }
        }
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
    void onEditProfileButtonClick(ActionEvent event) {
        if (!isEditing) {
            isEditing = true;
            editProfileButton.setText(I18n.get("save"));

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
        } else {
            isEditing = false;
            editProfileButton.setText(I18n.get("edit"));

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

    @FXML
    void onDeleteCardButtonClick(ActionEvent event) {
        User loggedUser = Session.getLoggedUser();
        if (loggedUser != null && loggedUser.getCardNumber() != null && !loggedUser.getCardNumber().isEmpty()) {
            var alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete your card?");
            var result = alert.showAndWait();
            if ((result.get() == javafx.scene.control.ButtonType.OK)) {
                loggedUser.setCardNumber(null);
                HibernateUserDao userDao = new HibernateUserDao();
                userDao.save(loggedUser);
                addEditCardButton.setText(I18n.get("add"));
            }
        }
        loadUserData();
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
                loginController.init(this.authService, () -> {
                });
            }

            Scene scene = new Scene(rootPane);
            mainStage.setScene(scene);
            mainStage.setTitle("Droppy");
            mainStage.show();
        }
    }


    @FXML
    private void initialize() {
        //zmena jazyka
        languageCB1.getItems().setAll(Language.values());
        languageCB1.setValue(Session.getCurrentLanguage());

        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        languageCB1.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Session.setCurrentLanguage(newVal);
                I18n.setLocale(new Locale(newVal.getCode()));
                updateText();
            }
        });

        //limit znakov na textove polia
        cardNumberTF.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 16 ? change : null));
        monthTF.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2 ? change : null));
        yearTF.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2 ? change : null));
        CCVTF.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 3 ? change : null));

        loadUserData();
    }

    //pre zmenu jazyka -musim este zrobit
    private void updateText() {
        profileLabel.setText(I18n.get("profile"));
        phoneNumberLabel.setText(I18n.get("phone_number"));
        creditDebitCardLabel.setText(I18n.get("credit_debit_card"));
        settingLabel.setText(I18n.get("settings"));
        languageLabel.setText(I18n.get("language"));
        deleteProfileLabel.setText(I18n.get("delete_profile"));
        logOutLabel.setText(I18n.get("log_out"));


        editProfileButton.setText(isEditing ? I18n.get("save") : I18n.get("edit"));

        User user = Session.getLoggedUser();
        if (isAddingEditingCard) {
            addEditCardButton.setText(I18n.get("save"));
        } else {
            if (user == null || user.getCardNumber() == null || user.getCardNumber().isEmpty()) {
                addEditCardButton.setText(I18n.get("add"));
            } else {
                addEditCardButton.setText(I18n.get("edit"));
            }
        }

        deleteProfileButton.setText(I18n.get("delete"));
        deleteCardButton.setText(I18n.get("delete"));
        logOutButton.setText(I18n.get("log_out"));
    }

    //nacitanie info daneho prihlaseneho usera
    private void loadUserData() {
        User user = Session.getLoggedUser();
        //info o userovi
        if (user != null) {
            nameText.setText(user.getName());
            surnameText.setText(user.getSurname());
            userEmailDemo.setText(user.getEmail());
            if (user.getPhoneNumber() != null) {
                userPhoneDemo.setText(user.getPhoneNumber());
            }


            //toto je na to, ze ked uz ma ulozenu kartu, nech ukazuje rovno edit a nie add
            if (user.getCardNumber() == null || user.getCardNumber().isEmpty()) {
                addEditCardButton.setText(I18n.get("add"));
                deleteCardButton.setVisible(false);
                noCardLabel.setText(I18n.get("no_card"));
            } else {
                addEditCardButton.setText(I18n.get("edit"));
                noCardLabel.setText("************" + "/" + "**" + "/" + "**");
                deleteCardButton.setVisible(true);
            }
            isAddingEditingCard = false;
        }
    }


}
