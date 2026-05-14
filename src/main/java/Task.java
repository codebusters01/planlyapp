import java.time.LocalDate;

public class Task {
    private String description;
    private String priority;
    private LocalDate date;
    private boolean completed;
    private String user;

    // Constructor for creating a new task
    public Task(String description, String priority, LocalDate date) {
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.completed = false;
        this.user = Main.currentUsername;
    }

    // Constructor for loading an existing task from the file
    public Task(String description, String priority, LocalDate date, boolean completed, String user) {
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.completed = completed;
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getUser() {
        return user;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Formats the task so it can be saved in tasks.csv
    // Format: description,priority,date,completed,user
    public String toFileString() {
        return description + "," + priority + "," + date.toString() + "," + completed + "," + user;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Due: %s)", priority, description, date);
    }
}