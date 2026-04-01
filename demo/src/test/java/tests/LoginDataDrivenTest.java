package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.EnvReader;

public class LoginDataDrivenTest implements ITest {

    private String testName = "";

    @Override
    public String getTestName() {
        return testName;
    }

    private WebDriver createDriver(String browser, String env) {
        System.setProperty("env", env);
        WebDriver webDriver = DriverFactory.createDriver(browser);
        webDriver.get(ConfigReader.getInstance().get("base.url"));
        return webDriver;
    }

    // 🔥 Hàm xử lý password
    private String resolvePassword(String pass) {
        if (pass == null) {
            return EnvReader.getPassword(); // Chỉ resolve nếu null (not specified in test data)
        }
        return pass; // Giữ nguyên "" hoặc giá trị từ test data
    }

    @Test(dataProvider = "smokeData", dataProviderClass = utils.DataProviderUtils.class, groups = "smoke")
    public void testLoginSmoke(String user, String pass, String expected, String desc) {
        testName = desc;

        String realPass = resolvePassword(pass);

        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = createDriver(browser, System.getProperty("env", "dev"));

        try {
            LoginPage loginPage = new LoginPage(driver);
            boolean result = loginPage.login(user, realPass)
                    .isLoaded();

            Assert.assertTrue(result, desc);
        } finally {
            driver.quit();
        }
    }

    @Test(dataProvider = "allData", dataProviderClass = utils.DataProviderUtils.class, groups = "regression")
    public void testLoginAll(String user, String pass, String expected, String desc) {
        testName = desc;

        String realPass = resolvePassword(pass);
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = createDriver(browser, System.getProperty("env", "dev"));

        try {
            LoginPage loginPage = new LoginPage(driver);

            String normalizedExpected = expected == null ? "" : expected.trim().toLowerCase();

            if (normalizedExpected.contains("inventory")) {
                boolean result = loginPage.login(user, realPass)
                        .isLoaded();
                Assert.assertTrue(result, desc);
            } else {
                LoginPage failedPage = loginPage.loginExpectingFailure(user, realPass);
                String error = failedPage.getErrorMessage();

                if ("error".equals(normalizedExpected)) {
                    Assert.assertTrue(failedPage.isErrorDisplayed() && !error.isBlank(), desc);
                } else {
                    Assert.assertTrue(error.contains(expected), desc);
                }
            }
        } finally {
            driver.quit();
        }
    }
}