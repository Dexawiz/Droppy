package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import com.example.droppy.service.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.List;


public class HomeController {

    private AuthService authService;

    public void init(AuthService authService) {
        this.authService = authService;

        //Companies
        CompanyDao companyDao = new HibernateCompanyDao();
        List<Company> companies = companyDao.findAll();
        int index = 0;
        for (Company company : companies) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/CompanyComponent.fxml"));
                Node companyComponent = loader.load();

                CompanyComponentController controller = loader.getController();
                if (controller != null) {
                    controller.init(company);
                }

                if (index % 3==0) {
                    column1VBOX.getChildren().add(companyComponent);
                }else if (index % 3==1) {
                    column2VBOX.getChildren().add(companyComponent);
                }else{
                   column3VBOX.getChildren().add(companyComponent);
                }

                index++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Categories
        for (Category category : Category.getAllCategories()) {
            CheckBox checkBox = new CheckBox(category.name());
            checkBox.setFont(Font.font(14));
            checkBox.setOnAction(this::onCategoryItemCheckBoxClicked);
            chceckBoxHBox.getChildren().add(checkBox);
        }
    }

    @FXML
    private Circle avatarSmall;

    @FXML
    private Label categoriesLabel;

    @FXML
    private HBox chceckBoxHBox;

    @FXML
    private VBox column1VBOX;

    @FXML
    private VBox column2VBOX;

    @FXML
    private VBox column3VBOX;

    @FXML
    private Label droppyTextLogo;

    @FXML
    private Label restaurantsLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchTextField;

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

    @FXML
    void onCategoryItemCheckBoxClicked(ActionEvent event) {
        CheckBox source = (CheckBox) event.getSource();
        System.out.println(source.getText() + " is " + (source.isSelected() ? "selected" : "deselected"));
    }

}
