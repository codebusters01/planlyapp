# planlyapp
Planly is a simple task management app that helps users stay organized by creating tasks, setting dates, viewing upcoming activities, marking tasks as completed, and deleting tasks when needed. It also includes settings like dark mode and logout, making daily planning easier, cleaner, and more convenient.

## Requirements

- JDK 21
- Maven 3.9+

## Run the app

1. Clone the repository.
2. From the project root, run:

```bash
mvn javafx:run
```

The app stores user and task data in the local `data/` folder. Those CSV files are created automatically when a user signs up or creates tasks.

## Build the app

```bash
mvn clean package
```

## macOS note

If Maven reports that `JAVA_HOME` is not set correctly, point it at your JDK first:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```
