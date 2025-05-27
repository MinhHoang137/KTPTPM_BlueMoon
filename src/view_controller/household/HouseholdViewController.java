package view_controller.household;

import javafx.application.Application;
import javafx.stage.Stage;
import view_controller.ViewController;

public class HouseholdViewController extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewController.getInstance().openView(new HouseholdView(), "Hộ Khẩu", 1000, 700);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

