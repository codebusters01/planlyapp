import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;

public class Theme {

    private static final String LIGHT_BG = "#f7f5ef";
    private static final String LIGHT_HEADER = "#7aaacc";
    private static final String LIGHT_CARD = "#d9eaf5";
    private static final String LIGHT_TEXT = "#6c9ec9";
    private static final String LIGHT_BUTTON = "#2f5878";
    private static final String LIGHT_ROW = "white";

    private static final String DARK_BG = "#1a1a2e";
    private static final String DARK_HEADER = "#2e4a6a";
    private static final String DARK_CARD = "#16213e";
    private static final String DARK_TEXT = "#90c0e0";
    private static final String DARK_BUTTON = "#2e4a6a";
    private static final String DARK_ROW = "#24304d";
    private static final String DARK_NAV = "#0f0f1a";

    public static boolean isDark() {
        return SettingsModel.getInstance().isDarkMode();
    }

    public static String textColor() {
        return isDark() ? DARK_TEXT : LIGHT_TEXT;
    }

    public static String rowColor() {
        return isDark() ? DARK_ROW : LIGHT_ROW;
    }

    public static String taskTextStyle() {
        return "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + textColor() + ";";
    }

    public static String rowStyle() {
        return "-fx-background-color: " + rowColor() + "; -fx-background-radius: 10;";
    }

    public static void apply(Pane root, String activeTab) {
        boolean dark = isDark();
        root.setStyle("-fx-background-color: " + (dark ? DARK_BG : LIGHT_BG) + ";");
        applyNode(root, activeTab, dark);
    }

    private static void applyNode(Node node, String activeTab, boolean dark) {
        if (node instanceof Arc arc) {
            arc.setFill(Color.web(dark ? DARK_HEADER : LIGHT_HEADER));
        }

        if (node instanceof Label label) {
            String text = label.getText() == null ? "" : label.getText();

            if (text.equals("Planly")) {
                label.setStyle("-fx-font-size: 70px; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0.2, 3, 3);");
            } else {
                label.setStyle(label.getStyle() + "; -fx-text-fill: " + (dark ? DARK_TEXT : LIGHT_TEXT) + ";");
            }
        }

        if (node instanceof TextField || node instanceof PasswordField || node instanceof DatePicker || node instanceof MenuButton) {
            node.setStyle("-fx-background-color: " + (dark ? DARK_ROW : LIGHT_ROW) + "; -fx-text-fill: " + (dark ? "white" : "black") + "; -fx-background-radius: 10; -fx-border-radius: 10;");
        }

        if (node instanceof ListView<?>) {
            node.setStyle("-fx-background-color: " + (dark ? DARK_ROW : LIGHT_ROW) + "; -fx-background-radius: 10; -fx-control-inner-background: " + (dark ? DARK_ROW : LIGHT_ROW) + "; -fx-border-color: transparent;");
        }

        if (node instanceof Button button) {
            styleButton(button, activeTab, dark);
        }

        if (node instanceof ToggleButton toggleButton) {
            toggleButton.setStyle("-fx-background-color: " + (dark ? "#7aaacc" : "white") + "; -fx-text-fill: " + (dark ? "white" : LIGHT_BUTTON) + "; -fx-font-size: 15px; -fx-background-radius: 10; -fx-cursor: hand;");
        }

        if (node instanceof Pane pane && !(pane instanceof AnchorPane)) {
            if (pane instanceof HBox hbox && hbox.getLayoutY() >= 780) {
                hbox.setStyle("-fx-background-color: " + (dark ? DARK_NAV : LIGHT_ROW) + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, -2);");
            } else if (!(pane instanceof HBox) && pane.getLayoutY() >= 200 && pane.getPrefWidth() >= 300) {
                pane.setStyle("-fx-background-color: " + (dark ? DARK_CARD : LIGHT_CARD) + "; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.2, 0, 4);");
            }
        }

        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyNode(child, activeTab, dark);
            }
        }
    }

    private static void styleButton(Button button, String activeTab, boolean dark) {
        String text = button.getText() == null ? "" : button.getText();

        if (text.equals("Home") || text.equals("Tasks") || text.equals("Settings")) {
            boolean active = text.equalsIgnoreCase(activeTab);

            button.setStyle(
                    "-fx-background-color: " + (active ? (dark ? DARK_BUTTON : LIGHT_CARD) : "transparent") + ";" +
                            "-fx-text-fill: " + (active ? (dark ? "#dddddd" : LIGHT_BUTTON) : "#8d8d8d") + ";" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 12px;" +
                            "-fx-background-radius: 15;" +
                            "-fx-cursor: hand;"
            );
        } else if (text.equals("Log Out") || text.equals("Add Task")) {
            button.setStyle("-fx-background-color: " + (dark ? DARK_BUTTON : LIGHT_BUTTON) + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 10; -fx-cursor: hand;");
        } else if (text.startsWith("Sort") || text.equals("Reset")) {
            button.setStyle("-fx-background-color: " + (dark ? DARK_ROW : LIGHT_ROW) + "; -fx-text-fill: " + (dark ? DARK_TEXT : LIGHT_BUTTON) + "; -fx-font-size: 11px; -fx-background-radius: 8; -fx-border-color: " + (dark ? DARK_TEXT : LIGHT_BUTTON) + "; -fx-border-radius: 8; -fx-cursor: hand;");
        }
    }
}