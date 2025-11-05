package com.example.droppy.Navigator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Consumer;

public final class Navigator {
    private Navigator() {}

    public static Stage stageFrom(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static <T> T switchTo(ActionEvent event,
                                 String fxmlPath,
                                 String title,
                                 Class<T> controllerType,
                                 Consumer<T> init) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            T controller = controllerType.cast(loader.getController());
            if (init != null) init.accept(controller);

            Stage stage = stageFrom(event);
            stage.setScene(new Scene(root));
            if (title != null) stage.setTitle(title);
            stage.show();

            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to switch view: " + fxmlPath, e);
        }
    }
}
