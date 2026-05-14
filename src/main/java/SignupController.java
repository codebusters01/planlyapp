import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Passwords do not match.");
            return;
        }

        if (UserManager.userExists(username)) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Username already exists.");
            return;
        }

        UserManager.addUser(username, password);
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Account created.");
    }

    @FXML
    public void handleBack(ActionEvent event) throws Exception {
        Main.switchScene(event, "login.fxml");
    }
}