package utils;

public class EnvReader {

    public static String getUsername() {
        String username = System.getenv("APP_USERNAME");

        if (username == null || username.isBlank()) {
            username = ConfigReader.getInstance().get("app.username");
        }

        return username;
    }

    public static String getPassword() {
        String password = System.getenv("APP_PASSWORD");

        if (password == null || password.isBlank()) {
            password = ConfigReader.getInstance().get("app.password");
        }

        return password;
    }
}