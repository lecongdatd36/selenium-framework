package tests;

import base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class ParallelDataDrivenGridTest extends BaseTest {

    @DataProvider(name = "loginData", parallel = true)
    public Object[][] loginData() {

        return new Object[][]{
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"},
                {"visual_user", "secret_sauce"}
        };
    }

    @Test(dataProvider = "loginData")
    public void loginParallelGridTest(String username, String password) {

        getDriver().get(
                ConfigReader.getInstance().get("base.url")
        );

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login(username, password);

        System.out.println("User: " + username);
        System.out.println("Thread: " + Thread.currentThread().getId());
    }
}