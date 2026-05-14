import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleLogin(ActionEvent event) throws Exception {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Enter username and password.");
            return;
        }

        if (UserManager.validateLogin(username, password)) {
            Main.currentUsername = username;
            SettingsModel.getInstance().loadForCurrentUser();
            Main.switchScene(event, "home.fxml");
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Invalid login.");
        }
    }

    @FXML
    public void handleSignUp(ActionEvent event) throws Exception {
        Main.switchScene(event, "signup.fxml");
    }
}