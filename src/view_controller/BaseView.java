package view_controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public abstract class BaseView implements Initializable {
    protected final String RESOURCE_PATH = "/resources";
    public BaseView loadAndShow(Stage stage, String title, int width, int height) {
        try {
            URL resource = getClass().getResource(getFxmlPath());
            if (resource == null) {
                resource = getClass().getResource(RESOURCE_PATH + getFxmlPath());
                return null;
            }
            System.out.println("Resource: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract String getFxmlPath();
}
