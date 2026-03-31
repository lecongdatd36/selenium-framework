package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp(@Optional("chrome") String browser,
            @Optional("dev") String env) {

        // Must set env before using ConfigReader
        System.setProperty("env", env);

        // Load config
        ConfigReader config = ConfigReader.getInstance();

        String url = config.get("base.url");
        int wait = config.getInt("explicit.wait");

        System.out.println("Dang dung moi truong: " + env);
        System.out.println("URL: " + url);
        System.out.println("WAIT: " + wait);

        WebDriver webDriver;

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // chạy headless cho CI
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");

            webDriver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            webDriver = new FirefoxDriver();
        } else {
            throw new RuntimeException("Browser khong ho tro: " + browser);
        }

        webDriver.manage().window().maximize();

        // Do not hardcode wait
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        driver.set(webDriver);

        // Use URL from config
        getDriver().get(url);
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver currentDriver = getDriver();

        if (result.getStatus() == ITestResult.FAILURE && currentDriver != null) {
            takeScreenshot(result.getName(), currentDriver);
        }

        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove();
        }
    }

    private void takeScreenshot(String testName, WebDriver currentDriver) {
        File src;
        try {
            src = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.FILE);
        } catch (WebDriverException e) {
            System.out.println("Khong the chup screenshot vi session da dong: " + e.getMessage());
            return;
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String path = "target/screenshots/" + testName + "_" + timestamp + ".png";

        try {
            File dest = new File(path);
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}