package com.example.droppy.controller;

import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.service.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import com.example.droppy.domain.entity.User;

public class ProfileController {

    @FXML
    private Button addCreditDebitCardButton;

    @FXML
    private Circle avatarSmall;

    @FXML
    private Circle bigAvatar;

    @FXML
    private Label countryLabel;

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
    void onAddCreditDebitCardButtonClick(ActionEvent event) {

    }

    @FXML
    void onDeleteProfileButtonClick(ActionEvent event) {

    }

    @FXML
    void onEditCreditDebitCardButtonClick(ActionEvent event) {

    }

    @FXML
    void onEditProfileButtonClick(ActionEvent event) {

    }

    @FXML
    void onLogOutButtonClick(ActionEvent event) {

    }

    @FXML
    private void initialize() {
        User user = Session.getLoggedUser();
        if(user!=null){
            nameText.setText(user.getName());
            surnameText.setText(user.getSurname());
            userEmailDemo.setText(user.getEmail());
            if(user.getPhoneNumber()!=null){
                userPhoneDemo.setText(user.getPhoneNumber());
            }
        }
    }

}
