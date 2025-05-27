package view_controller.temporary;

import javafx.application.Application;
import javafx.stage.Stage;
import view_controller.ViewController;

public class TempViewTest extends Application {
    public static void main(String[] args) {
        // This is a placeholder for testing purposes.
        // You can implement your test logic here.
        System.out.println("Temporary View Test is running.");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewController viewController = ViewController.getInstance();
        viewController.openView(new TempView(), "Tạm trú tạm vắng", 800, 600);
    }
}
