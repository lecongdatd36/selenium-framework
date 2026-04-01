package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class ParallelGridTest extends BaseTest {

    @Test
    public void loginTest1() {

        getDriver().get(ConfigReader.getInstance().get("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("standard_user", "secret_sauce");

        System.out.println("Test 1 Thread: " + Thread.currentThread().getId());
    }

    @Test
    public void loginTest2() {

        getDriver().get(ConfigReader.getInstance().get("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("problem_user", "secret_sauce");

        System.out.println("Test 2 Thread: " + Thread.currentThread().getId());
    }

    @Test
    public void loginTest3() {

        getDriver().get(ConfigReader.getInstance().get("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("performance_glitch_user", "secret_sauce");

        System.out.println("Test 3 Thread: " + Thread.currentThread().getId());
    }

    @Test
    public void loginTest4() {

        getDriver().get(ConfigReader.getInstance().get("base.url"));

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("visual_user", "secret_sauce");

        System.out.println("Test 4 Thread: " + Thread.currentThread().getId());
    }
}