import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SettingsModel {

    private static final SettingsModel INSTANCE = new SettingsModel();

    public static SettingsModel getInstance() {
        return INSTANCE;
    }

    private final BooleanProperty darkMode = new SimpleBooleanProperty(false);

    public void loadForCurrentUser() {
        darkMode.set(Main.currentUsername != null && UserManager.getDarkMode(Main.currentUsername));
    }

    public boolean isDarkMode() {
        return darkMode.get();
    }

    public void setDarkMode(boolean value) {
        darkMode.set(value);

        if (Main.currentUsername != null) {
            UserManager.saveDarkMode(Main.currentUsername, value);
        }
    }

    public BooleanProperty darkModeProperty() {
        return darkMode;
    }
}