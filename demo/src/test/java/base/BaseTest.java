package base;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.DriverFactory;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @Parameters({ "browser", "env" })
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser,
            @Optional("dev") String env) {

        System.setProperty("env", env);

        ConfigReader config = ConfigReader.getInstance();

        String url = config.get("base.url");
        int wait = config.getInt("explicit.wait");

        System.out.println("=================================");
        System.out.println("Thread: " + Thread.currentThread().getId());
        System.out.println("Moi truong: " + env);
        System.out.println("Browser: " + browser);
        System.out.println("URL: " + url);

        WebDriver webDriver = DriverFactory.createDriver(browser);

        System.out.println("Dang chay Selenium Grid: " + System.getProperty("grid.url", "http://localhost:4444"));

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
        webDriver.manage().window().maximize();

        driver.set(webDriver);

        getDriver().get(url);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        WebDriver currentDriver = getDriver();

        if (result.getStatus() == ITestResult.FAILURE && currentDriver != null) {

            saveScreenshotFile(result.getName(), currentDriver);

            attachScreenshotToAllure(currentDriver);
        }

        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove();
        }
    }

    // lưu file PNG
    private void saveScreenshotFile(String testName, WebDriver currentDriver) {

        try {

            File src = ((TakesScreenshot) currentDriver)
                    .getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String path = "target/screenshots/" + testName + "_" + timestamp + ".png";

            File dest = new File(path);
            dest.getParentFile().mkdirs();

            Files.copy(src.toPath(), dest.toPath());

        } catch (IOException | WebDriverException e) {
            e.printStackTrace();
        }
    }

    // attach vào Allure
    @Attachment(value = "Screenshot when failed", type = "image/png")
    public byte[] attachScreenshotToAllure(WebDriver driver) {

        return ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
    }
}