package view_controller;

import javafx.application.Application;
import javafx.stage.Stage;
import view_controller.resident.ResidentView;

public class ViewController extends Application {
    private  static ViewController instance;
    @Override
    public void start(Stage stage) throws Exception {
        ResidentView residentView = new ResidentView();
        residentView.loadAndShow(stage, "Danh sách cư dân", 700, 700);
    }
    public BaseView openView(BaseView view, String title, int width, int height) {
        Stage stage = new Stage();
        return view.loadAndShow(stage, title, width, height);
    }


    public static void main(String[] args) {
        launch(args);
    }
    public static ViewController getInstance() {
        if (instance == null) {
            instance = new ViewController();
        }
        return instance;
    }
}
