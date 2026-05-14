import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static final String FILE_NAME = "data/users.csv";

    // Adds a new user and starts dark mode as false by default
    public static void addUser(String username, String password) {
        File file = new File(FILE_NAME);
        file.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() > 0) {
                bw.newLine();
            }

            bw.write(username + "," + password + ",false");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Checks if the username already exists
    public static boolean userExists(String username) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 1 && parts[0].equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Checks if the username and password match a saved user
    public static boolean validateLogin(String username, String password) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 2 &&
                        parts[0].equals(username) &&
                        parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Gets the saved dark mode setting for a user
    public static boolean getDarkMode(String username) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 1 && parts[0].equals(username)) {
                    return parts.length >= 3 && Boolean.parseBoolean(parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates the saved dark mode setting for a user
    public static void saveDarkMode(String username, boolean darkMode) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 2 && parts[0].equals(username)) {
                    line = parts[0] + "," + parts[1] + "," + darkMode;
                }

                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}