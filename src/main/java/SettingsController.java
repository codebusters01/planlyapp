import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

public class SettingsController {

    @FXML private AnchorPane root;
    @FXML private ToggleButton darkModeToggle;

    private final SettingsModel model = SettingsModel.getInstance();

    @FXML
    public void initialize() {
        model.loadForCurrentUser();
        darkModeToggle.setSelected(model.isDarkMode());
        Theme.apply(root, "settings");
    }

    @FXML
    private void handleDarkModeToggle() {
        model.setDarkMode(darkModeToggle.isSelected());
        Theme.apply(root, "settings");
    }

    @FXML
    private void handleLogOut(ActionEvent event) throws Exception {
        Main.currentUsername = null;
        Main.switchScene(event, "login.fxml");
    }

    @FXML
    private void handleHome(ActionEvent event) throws Exception {
        Main.switchScene(event, "home.fxml");
    }

    @FXML
    private void handleTasks(ActionEvent event) throws Exception {
        Main.switchScene(event, "tasks.fxml");
    }

    @FXML
    private void handleSettings() {
    }
}