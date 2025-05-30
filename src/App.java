
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view_controller.ViewController;
import view_controller.household.HouseholdView;
import view_controller.temporary.TempView;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/user/userManage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Blue Moon");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
