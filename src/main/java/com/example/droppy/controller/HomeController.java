package com.example.droppy.controller;

import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;


public class HomeController {

    private AuthService authService;

    public void init(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label categoriesLabel;

    @FXML
    private CheckBox categoryItemCheckBox;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Label restaurantsLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Pagination paginationCompany;

    @FXML
    void onCategoryItemCheckBoxClicked(ActionEvent event) {

    }

    @FXML
    void onProfileClick(MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/ProfileOrLogoutComponent.fxml"));
        VBox profileMenu = loader.load();

        Popup popup = new Popup();
        popup.getContent().add(profileMenu);
        popup.setAutoHide(true);

        Bounds bounds = avatarSmall.localToScreen(avatarSmall.getBoundsInLocal());
        popup.show(((Node) event.getSource()), event.getScreenX(), event.getScreenY());

    }


}
