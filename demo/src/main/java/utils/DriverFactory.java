package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {

        try {

            String gridUrl = System.getProperty("grid.url", "http://localhost:4444");

            if (browser.equalsIgnoreCase("firefox")) {

                FirefoxOptions options = new FirefoxOptions();

                return new RemoteWebDriver(
                        new URL(gridUrl + "/wd/hub"),
                        options);
            }

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            return new RemoteWebDriver(
                    new URL(gridUrl + "/wd/hub"),
                    options);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}