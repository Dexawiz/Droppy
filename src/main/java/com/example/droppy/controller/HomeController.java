package com.example.droppy.controller;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.repository.CompanyDao;
import com.example.droppy.repository.HibernateCompanyDao;
import com.example.droppy.service.AuthService;
import com.example.droppy.service.I18n;
import com.example.droppy.service.Session;
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
import java.util.Locale;


public class HomeController {

    private AuthService authService;
    private List<Company> allCompanies;

    public void init(AuthService authService) {
        this.authService = authService;
        I18n.setLocale(new Locale(Session.getCurrentLanguage().getCode()));
        updateText();

        //Companies
        CompanyDao companyDao = new HibernateCompanyDao();
        List<Company> companies = companyDao.findAll();
        allCompanies = companies;
        int index = 0;
        for (Company company : companies) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/CompanyComponent.fxml"));
                Node companyComponent = loader.load();

                CompanyComponentController controller = loader.getController();
                if (controller != null) {
                    controller.init(this.authService, company);
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
        CheckBox allCompaniesCheckBox = new CheckBox("All");
        allCompaniesCheckBox.setFont(Font.font(14));
        allCompaniesCheckBox.setOnAction(this::onCategoryItemCheckBoxClicked);
        chceckBoxHBox.getChildren().add(allCompaniesCheckBox);

        for (Category category : Category.getAllCategories()) {
            CheckBox checkBox = new CheckBox(category.getTranslated());
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
        CheckBox sourceCheckBox = (CheckBox) event.getSource();
        String categoryName = sourceCheckBox.getText();

        if(sourceCheckBox.isSelected()){
            for(Node node : chceckBoxHBox.getChildren()){
                if(node instanceof CheckBox checkBox){
                    if(!checkBox.getText().equals(categoryName)){
                        checkBox.setSelected(false);
                    }
                }
            }
        }

        if (sourceCheckBox.isSelected()) {
            if(categoryName.equals("All")){
                renderCompanies(allCompanies);
                return;
            }
            List<Company> filteredCompanies = allCompanies.stream()
                    .filter(company -> company.getCategory().name().equals(categoryName))
                    .toList();
            renderCompanies(filteredCompanies);
        } else {
            renderCompanies(allCompanies);
        }
    }

    private void renderCompanies(List<Company> companies) {
        column1VBOX.getChildren().clear();
        column2VBOX.getChildren().clear();
        column3VBOX.getChildren().clear();

        int index = 0;
        for (Company company : companies) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/CompanyComponent.fxml"));
                Node companyComponent = loader.load();

                CompanyComponentController controller = loader.getController();
                if (controller != null) {
                    controller.init(authService, company);
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
    }

    private void updateText(){
        restaurantsLabel.setText(I18n.get("restaurant"));
        categoriesLabel.setText(I18n.get("categories"));
    }

}
