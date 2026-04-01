package base;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class WebDriverListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Not needed
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Lifecycle is handled by BaseTest @BeforeMethod.
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Lifecycle is handled by BaseTest @AfterMethod.
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Lifecycle is handled by BaseTest @AfterMethod.
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Not needed
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not needed
    }

    @Override
    public void onFinish(ITestContext context) {
        // Not needed
    }
}
