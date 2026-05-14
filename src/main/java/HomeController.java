import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeController {

    @FXML private AnchorPane root;
    @FXML private Label welcomeLabel;
    @FXML private VBox highTasksBox;
    @FXML private VBox todayTasksBox;

    private final String filePath = "data/tasks.csv";

    @FXML
    public void initialize() {
        SettingsModel.getInstance().loadForCurrentUser();
        Theme.apply(root, "home");

        if (Main.currentUsername != null) {
            welcomeLabel.setText("Welcome Back, " + Main.currentUsername + "!");
        } else {
            welcomeLabel.setText("Welcome Back!");
        }

        loadTasks();
    }

    // Loads high priority tasks and today's tasks onto the home screen
    private void loadTasks() {
        highTasksBox.getChildren().clear();
        todayTasksBox.getChildren().clear();

        List<Task> tasks = readTasks();
        int highCount = 0;
        int todayCount = 0;

        for (Task task : tasks) {
            if (task.isCompleted()) {
                continue;
            }

            if (task.getPriority().equalsIgnoreCase("High") && highCount < 2) {
                highTasksBox.getChildren().add(createTaskRow(task, true));
                highCount++;
            }

            if (task.getDate().equals(LocalDate.now())) {
                todayTasksBox.getChildren().add(createTaskRow(task, false));
                todayCount++;
            }
        }

        if (highCount == 0) {
            highTasksBox.getChildren().add(emptyRow("No high priority tasks"));
        }

        if (todayCount == 0) {
            todayTasksBox.getChildren().add(emptyRow("No tasks due today"));
        }
    }

    // Reads tasks.csv and returns only the current user's tasks
    private List<Task> readTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");

                if (parts.length >= 5 && parts[4].equals(Main.currentUsername)) {
                    tasks.add(new Task(
                            parts[0],
                            parts[1],
                            LocalDate.parse(parts[2]),
                            Boolean.parseBoolean(parts[3]),
                            parts[4]
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    // Creates a task row with a checkbox, priority circle, task name, and optional due date
    private HBox createTaskRow(Task task, boolean showDate) {
        CheckBox checkBox = new CheckBox();

        Circle circle = new Circle(6);
        circle.setFill(getPriorityColor(task.getPriority()));

        String text = task.getDescription();

        if (showDate) {
            text += " - Due: " + task.getDate();
        }

        Label label = new Label(text);
        label.setPrefWidth(250);
        label.setStyle(Theme.taskTextStyle());

        HBox row = new HBox(10, checkBox, circle, label);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle(Theme.rowStyle());
        row.setPrefSize(320, 45);
        row.setMinSize(320, 45);
        row.setMaxSize(320, 45);

        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) {
                markTaskCompleted(task);
                loadTasks();
            }
        });

        return row;
    }

    // Creates a placeholder row when there are no tasks to show
    private HBox emptyRow(String text) {
        Label label = new Label(text);
        label.setPrefWidth(290);
        label.setStyle(Theme.taskTextStyle());

        HBox row = new HBox(label);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setStyle(Theme.rowStyle());
        row.setPrefSize(320, 45);
        row.setMinSize(320, 45);
        row.setMaxSize(320, 45);

        return row;
    }

    // Marks a task as completed in tasks.csv
    private void markTaskCompleted(Task taskToComplete) {
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        List<String> lines = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 5
                        && parts[0].equals(taskToComplete.getDescription())
                        && parts[1].equals(taskToComplete.getPriority())
                        && parts[2].equals(taskToComplete.getDate().toString())
                        && parts[4].equals(Main.currentUsername)) {
                    parts[3] = "true";
                    line = String.join(",", parts);
                }

                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    // Returns the display color for each task priority
    private Color getPriorityColor(String priority) {
        return switch (priority.toLowerCase()) {
            case "high" -> Color.web("#E42217");
            case "medium" -> Color.web("#FFC107");
            default -> Color.web("#28A745");
        };
    }

    @FXML
    public void handleTasks(ActionEvent event) throws Exception {
        Main.switchScene(event, "tasks.fxml");
    }

    @FXML
    public void handleSettings(ActionEvent event) throws Exception {
        Main.switchScene(event, "settings.fxml");
    }
}