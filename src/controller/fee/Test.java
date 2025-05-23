package controller.fee;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        URL resource = getClass().getResource("/view/fee/feeManager.fxml");
        System.out.println("Resource: " + resource);
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();
        stage.setTitle("title");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();

    }
}
