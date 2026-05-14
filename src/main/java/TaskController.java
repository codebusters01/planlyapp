import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TaskController {

    @FXML private AnchorPane root;
    @FXML private TextField taskInput;
    @FXML private MenuButton priorityMenu;
    @FXML private DatePicker datePicker;
    @FXML private ListView<Task> taskListView;

    private ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private String selectedPriority = "Low";

    // Path to the task data file
    private final String FILE_PATH = "data/tasks.csv";

    // Tracks the current sorting mode so it can be reapplied after changes
    private String currentSortMode = "DEFAULT";

    @FXML
    public void initialize() {
        SettingsModel.getInstance().loadForCurrentUser();
        Theme.apply(root, "tasks");

        taskListView.setItems(allTasks);

        // Customizes how each task appears in the ListView
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(task.isCompleted());

                    Circle priorityIcon = createPriorityBadge(task.getPriority());

                    String taskText = String.format("%s (Due: %s)", task.getDescription(), task.getDate());
                    Label label = new Label(taskText);
                    label.setStyle(Theme.taskTextStyle());

                    if (task.isCompleted()) {
                        label.setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
                        priorityIcon.setOpacity(0.4);
                    }

                    // Updates completion status when the checkbox is clicked
                    checkBox.setOnAction(e -> {
                        task.setCompleted(checkBox.isSelected());
                        saveAllTasksToFile();
                        applyCurrentSort();
                        taskListView.refresh();
                    });

                    // Delete button with a warning if the task is incomplete
                    Button deleteBtn = new Button("✕");
                    deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e42217; -fx-font-weight: bold; -fx-cursor: hand;");

                    deleteBtn.setOnAction(e -> {
                        if (!task.isCompleted()) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Task");
                            alert.setHeaderText("This task is not completed yet.");
                            alert.setContentText("Are you sure you want to delete: '" + task.getDescription() + "'?");

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.isPresent() && result.get() != ButtonType.OK) {
                                return;
                            }
                        }

                        allTasks.remove(task);
                        saveAllTasksToFile();
                        applyCurrentSort();
                    });

                    // Spacer pushes the delete button to the right side
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox hbox = new HBox(10, checkBox, priorityIcon, label, spacer, deleteBtn);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setStyle("-fx-background-color: " + Theme.rowColor() + ";");

                    setStyle("-fx-background-color: " + Theme.rowColor() + ";");
                    setGraphic(hbox);
                }
            }
        });

        // Updates selectedPriority when a menu item is chosen
        for (MenuItem item : priorityMenu.getItems()) {
            item.setOnAction(e -> {
                selectedPriority = item.getText();
                priorityMenu.setText(selectedPriority);
            });
        }

        loadTasksFromFile();
        applyCurrentSort();
    }

    @FXML
    void handleCreateTask() {
        String desc = taskInput.getText().trim();
        LocalDate date = datePicker.getValue();

        if (!desc.isEmpty() && date != null) {
            Task newTask = new Task(desc, selectedPriority, date);

            allTasks.add(newTask);
            saveAllTasksToFile();
            applyCurrentSort();

            taskInput.clear();
            datePicker.setValue(null);
            priorityMenu.setText("Priority");
            selectedPriority = "Low";
        }
    }

    // Saves all current user's tasks while keeping other users' tasks
    private void saveAllTasksToFile() {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        List<String> otherUsersTasks = new ArrayList<>();

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");

                    if (parts.length >= 5) {
                        String taskUser = parts[4];

                        if (!taskUser.equals(Main.currentUsername)) {
                            otherUsersTasks.add(line);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e.getMessage());
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (String line : otherUsersTasks) {
                writer.println(line);
            }

            for (Task task : allTasks) {
                writer.println(task.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Loads only the tasks that belong to the currently logged-in user
    private void loadTasksFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 5) {
                    String description = parts[0];
                    String priority = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    boolean completed = Boolean.parseBoolean(parts[3]);
                    String user = parts[4];

                    if (user.equals(Main.currentUsername)) {
                        Task loadedTask = new Task(description, priority, date, completed, user);
                        allTasks.add(loadedTask);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    // Sorts incomplete tasks first, then applies the selected sort mode
    private void applyCurrentSort() {
        Comparator<Task> primarySort = Comparator.comparing(Task::isCompleted);
        Comparator<Task> secondarySort = (t1, t2) -> 0;

        if ("DATE".equals(currentSortMode)) {
            secondarySort = Comparator.comparing(Task::getDate);
        } else if ("PRIORITY".equals(currentSortMode)) {
            secondarySort = (t1, t2) -> {
                int p1 = getPriorityWeight(t1.getPriority());
                int p2 = getPriorityWeight(t2.getPriority());
                return Integer.compare(p2, p1);
            };
        }

        FXCollections.sort(allTasks, primarySort.thenComparing(secondarySort));
    }

    @FXML
    void handleSortByImportance() {
        currentSortMode = "PRIORITY";
        applyCurrentSort();
    }

    @FXML
    void handleSortByDate() {
        currentSortMode = "DATE";
        applyCurrentSort();
    }

    @FXML
    void handleReset() {
        currentSortMode = "DEFAULT";
        allTasks.clear();
        loadTasksFromFile();
        applyCurrentSort();
    }

    @FXML
    void handleHome(javafx.event.ActionEvent event) throws Exception {
        Main.switchScene(event, "home.fxml");
    }

    @FXML
    void handleTasks(javafx.event.ActionEvent event) {
    }

    @FXML
    void handleSettings(javafx.event.ActionEvent event) throws Exception {
        Main.switchScene(event, "settings.fxml");
    }

    // Creates a colored circle based on task priority
    private Circle createPriorityBadge(String priority) {
        Circle circle = new Circle(6);

        Color baseColor;

        switch (priority.toLowerCase()) {
            case "high":
                baseColor = Color.web("#E42217");
                break;
            case "medium":
                baseColor = Color.web("#FFC107");
                break;
            case "low":
            default:
                baseColor = Color.web("#28A745");
                break;
        }

        RadialGradient gradient = new RadialGradient(
                0, 0, 0.3, 0.3, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, baseColor.brighter().brighter()),
                new Stop(1, baseColor.darker())
        );

        circle.setFill(gradient);
        circle.setStroke(Color.DARKGRAY);
        circle.setStrokeWidth(0.5);

        return circle;
    }

    // Gives each priority a numeric value for sorting
    private int getPriorityWeight(String p) {
        return switch (p.toLowerCase()) {
            case "high" -> 3;
            case "medium" -> 2;
            default -> 1;
        };
    }
}