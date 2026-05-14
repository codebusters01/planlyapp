import java.util.Objects;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static String currentUsername;

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/login.fxml"))));
        stage.setTitle("Planly");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void switchScene(ActionEvent event, String fxmlFile) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/" + fxmlFile))));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}