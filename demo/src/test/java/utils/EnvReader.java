package utils;

public class EnvReader {

    public static String getUsername() {
        String username = System.getenv("APP_USERNAME");

        if (username == null || username.isBlank()) {
            throw new RuntimeException(
                "❌ APP_USERNAME not found! \n" +
                "Set env var: export APP_USERNAME='standard_user'\n" +
                "Or on GitHub: Create secret SAUCEDEMO_USERNAME"
            );
        }

        return username;
    }

    public static String getPassword() {
        String password = System.getenv("APP_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new RuntimeException(
                "❌ APP_PASSWORD not found! \n" +
                "Set env var: export APP_PASSWORD='secret_sauce'\n" +
                "Or on GitHub: Create secret SAUCEDEMO_PASSWORD"
            );
        }

        return password;
    }
}