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
import javafx.stage.Stage;


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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // get controller and pass AuthService so the component can call logout / navigate
        ProfileOrLogOutComponentController controller = loader.getController();
        if (controller != null) {
            controller.init(this.authService, stage);
        }

        Popup popup = new Popup();
        popup.getContent().add(profileMenu);
        popup.setAutoHide(true);

        // position popup under the avatar
        Bounds bounds = avatarSmall.localToScreen(avatarSmall.getBoundsInLocal());
        double x = bounds != null ? bounds.getMinX() : event.getScreenX();
        double y = bounds != null ? bounds.getMaxY() : event.getScreenY();
        popup.show(((Node) event.getSource()), x, y);

    }


}
