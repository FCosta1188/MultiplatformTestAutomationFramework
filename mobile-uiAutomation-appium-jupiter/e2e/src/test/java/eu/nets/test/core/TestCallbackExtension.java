package eu.nets.test.core;

import eu.nets.test.util.AllureUtil;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import static eu.nets.test.util.AllureUtil.logError;

public class TestCallbackExtension implements AfterTestExecutionCallback {
    private final Supplier<WebDriver> driverSupplier;
    private final Supplier<Path> reportDirSupplier;

    public TestCallbackExtension(Supplier<WebDriver> driverSupplier, Supplier<Path> reportDirSupplier) {
        this.driverSupplier = driverSupplier;
        this.reportDirSupplier = reportDirSupplier;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Optional<Throwable> failure = context.getExecutionException();

        if (failure.isPresent()) {
            WebDriver driver = driverSupplier.get();
            Path reportDir = reportDirSupplier.get();

            if (driver instanceof TakesScreenshot) {
                try {
                    String testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9.-]", "_");
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    AllureUtil.attachImage(testName, "png", screenshot);
                } catch (Exception e) {
                    logError(e.getMessage());
                }
            }

            //TODO: delete OTP emails to avoid failures in subsequent tests
        }
    }
}
