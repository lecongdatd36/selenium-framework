package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.LoginPage;
import utils.EnvReader;
import utils.TestDataFactory;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test
    public void testCheckoutRandom() {

        // ✅ lấy data random
        Map<String, String> data = TestDataFactory.randomCheckoutData();

        System.out.println("FirstName: " + data.get("firstName"));
        System.out.println("LastName: " + data.get("lastName"));
        System.out.println("PostalCode: " + data.get("postalCode"));

        // ⚠️ đảm bảo driver không null
        Assert.assertNotNull(getDriver(), "Driver is null - setup failed");

        // ✅ flow test
        CheckoutPage checkoutPage = new LoginPage(getDriver())
                .login(EnvReader.getUsername(), EnvReader.getPassword())
                .addFirstItemToCart()
                .goToCart()
                .goToCheckout();

        // ✅ verify page load
        Assert.assertTrue(checkoutPage.isLoaded(),
                "Checkout information page should be loaded");

        // ✅ fill form
        checkoutPage.fillCheckoutInfo(
                data.get("firstName"),
                data.get("lastName"),
                data.get("postalCode"));
    }
}