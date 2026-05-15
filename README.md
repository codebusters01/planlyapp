# Planly

Planly is a JavaFX task management application that helps users sign up, log in, create tasks, assign priorities, set due dates, mark tasks as completed, and switch between light and dark mode. The app uses file-based persistence so user accounts, dark mode preferences, and task data remain available between sessions.

## Contributors

- Khang Nguyen
- Janet Yeboah
- Darmierra - Joy Mbu Besong
- Drake Barrientos

## MVC Structure

- Model: `Task`, `UserManager`, `SettingsModel`
- View: `login.fxml`, `signup.fxml`, `home.fxml`, `tasks.fxml`, `settings.fxml`
- Controller: `LoginController`, `SignupController`, `HomeController`, `TaskController`, `SettingsController`
- Application bootstrap and scene switching: `Main`
- Shared styling helper: `Theme`

## Data Files

The app automatically creates `data/users.csv` and `data/tasks.csv` on first use. No setup needed.

## Requirements

- JDK 21
- Maven 3.9 or newer
- Internet access the first time Maven runs so it can download JavaFX dependencies

## How to Run

1. Clone the repository or download the ZIP from GitHub.
2. Open a terminal in the project root.
3. If needed on macOS, set `JAVA_HOME`:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

4. Run the JavaFX application:

```bash
mvn javafx:run
```

## How to Build

```bash
mvn clean package
```

## Notes for Testing

- The application does not require any manually created data files.
- The `data/` directory is already present in the repository, and the CSV files are generated automatically when the app is used.
- If the project is tested from a fresh clone, the first Maven run may take longer because dependencies must be downloaded.

## Known Issues

- Tasks cannot be edited after creation, only deleted
- Dark mode may require navigating between screens to fully apply
