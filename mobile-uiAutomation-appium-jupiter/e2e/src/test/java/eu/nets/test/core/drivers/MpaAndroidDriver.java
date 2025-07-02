package eu.nets.test.core.drivers;

import eu.nets.test.core.enums.MpaWidget;
import eu.nets.test.core.enums.PathKey;
import eu.nets.test.util.AllureUtil;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.imagecomparison.FeatureDetector;
import io.appium.java_client.imagecomparison.FeaturesMatchingOptions;
import io.appium.java_client.imagecomparison.FeaturesMatchingResult;
import io.appium.java_client.imagecomparison.MatchingFunction;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static eu.nets.test.core.enums.MpaWidget.ALERT_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ALERT_POPUP;
import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;

public class MpaAndroidDriver extends AndroidDriver implements MpaDriver, TestWatcher {

    public static final String MPA_APP_ID = PropertiesUtil.ENV.getProperty("mpaAppId"); // bundleId
    public static final Path MPA_APK_DIR = PathKey.MPA.resolve().asPath();
    public static final int SLEEP_MS = 1000;

    public MpaAndroidDriver(int waitS, boolean setAppiumApp) throws MalformedURLException {
        super(
                new URL(PropertiesUtil.CONFIG.getProperty("appium.driver.remoteAddress")),
                EnvUtil.getDesideredCapabilities(waitS, setAppiumApp)
        );
    }

    public MpaAndroidDriver(int waitS, boolean setAppiumApp, String language, String locale) throws MalformedURLException {
        super(
                new URL(PropertiesUtil.CONFIG.getProperty("appium.driver.remoteAddress")),
                EnvUtil.getDesideredCapabilities(waitS, setAppiumApp, language, locale)
        );
    }

    @Override
    public boolean isNativeAppContext() {
        String appContext = this.getContext();
        assert appContext != null;
        return appContext.equals("NATIVE_APP");
    }

    @Override
    public boolean waitUntilCondition(Supplier<Boolean> condition, int waitS) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        try {
            wait.until(driver -> condition.get());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean waitUntilCondition(ExpectedCondition<Boolean> condition, int waitS) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        try {
            wait.until(condition);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public void handleSystemAlert() { // Android system alerts, e.g: System UI isn't responding, Gboard isn't responding, etc.
        try {
            if (!Objects.requireNonNull(getContext()).equalsIgnoreCase("NATIVE_APP")) {
                return;
            }

            WebElement alert = this.findElement(ALERT_POPUP.byAndroidResourceId());

            if (alert.isDisplayed() || alert.isEnabled()) {
                Allure.step("Alert detected!!!", () -> {
                    byte[] screenshot = this.getScreenshotAs(OutputType.BYTES);
                    AllureUtil.attachImage("alert_" + new Date(), "png", screenshot);
                    Allure.addAttachment("Alert text", alert.getText());
                });

                EnvUtil.safeSleep(SLEEP_MS);

                try {
                    this.findElement(ALERT_CLOSE_BUTTON.byAndroidResourceId()).click();
                    return;
                } catch (NoSuchElementException nsee1) {
                    EnvUtil.safeSleep(SLEEP_MS);

                    try {
                        this.findElement(ALERT_CLOSE_BUTTON.byAndroidXpathWithResourceId()).click();
                        return;
                    } catch (TimeoutException | NoSuchElementException nsee2) {
                        EnvUtil.safeSleep(SLEEP_MS);

                        try {
                            this.findElement(ALERT_CLOSE_BUTTON.byAndroidXpathWithResourceId()).click();
                            return;
                        } catch (TimeoutException | NoSuchElementException nsee3) {
                            throw new NoSuchElementException(
                                    logError(
                                            String.format(
                                                    "ALERT_CLOSE_BUTTON not found: %s - Tried locators: ALERT_CLOSE_BUTTON.byId(), ALERT_CLOSE_BUTTON" +
                                                            ".byXpathWithId() and By.id(ALERT_CLOSE_BUTTON.getId())\n" +
                                                            "Exception: %s",
                                                    ALERT_CLOSE_BUTTON.getAndroidId(),
                                                    nsee3.getMessage()
                                            )
                                    )
                            );
                        }
                    }
                }
            }

            if (alert.isDisplayed() || alert.isEnabled()) {
                throw new IllegalStateException(logError("System alert popup still present after closing attempts - alert: " + alert.getText()));
            }
        } catch (NoSuchElementException noAlertDetected) {
            return;
        }
    }

    public WebElement getWidgetWebElement(MpaWidget widget) {
        WebElement element;
        By locator;
        try {
            locator = widget.byAndroidResourceId();
            element = this.findElement(locator);
            logInfo("Element found by widget.byResourceId() - locator: " + locator);
        } catch (TimeoutException | NoSuchElementException nsee1) {
            try {
                locator = widget.byAndroidXpathWithResourceId();
                element = this.findElement(locator);
                logInfo("Element found by widget.byXpathWithResourceId() - locator: " + locator);
            } catch (TimeoutException | NoSuchElementException nsee2) {
                try {
                    locator = By.id(widget.getAndroidId());
                    element = this.findElement(locator);
                    logInfo("Element found by By.id(widget.getId()) - locator: " + locator);
                } catch (TimeoutException | NoSuchElementException nsee3) {
                    throw new NoSuchElementException(
                            logError(
                                    String.format(
                                            "Element not found: %s - Exception: %s",
                                            widget.getAndroidId(),
                                            nsee3.getMessage()
                                    )
                            )
                    );
                }
            }
        }

        return element;
    }

    public WebElement safeGetWidgetWebElement(MpaWidget widget, int waitS) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        WebElement element;
        By locator;
        try {
            locator = widget.byAndroidResourceId();
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logInfo("Element found by widget.byResourceId() - locator: " + locator);
        } catch (TimeoutException | NoSuchElementException nsee1) {
            try {
                locator = widget.byAndroidXpathWithResourceId();
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                logInfo("Element found by widget.byXpathWithResourceId() - locator: " + locator);
            } catch (TimeoutException | NoSuchElementException nsee2) {
                try {
                    locator = By.id(widget.getAndroidId());
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    logInfo("Element found by By.id(widget.getId()) - locator: " + locator);
                } catch (TimeoutException | NoSuchElementException nsee3) {
                    throw new NoSuchElementException(
                            logError(
                                    String.format(
                                            "Element not found: %s - Exception: %s",
                                            widget.getAndroidId(),
                                            nsee3.getMessage()
                                    )
                            )
                    );
                }
            }
        }

        return element;
    }

    @Override
    public WebElement waitUntilElementVisible(By locator, int waitS) {
        handleSystemAlert();

        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        WebElement element;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logInfo("Element found by locator: " + locator);
        } catch (Exception e) {
            throw new NoSuchElementException(String.format("Element not found: %s - Waited %s seconds (polling every %s ms) - Exception: %s",
                    locator.toString(),
                    waitS,
                    SLEEP_MS,
                    e.getMessage()));
        }

        return element;
    }

    @Override
    public WebElement waitUntilElementVisible(MpaWidget widget, int waitS) {
        handleSystemAlert();

        return safeGetWidgetWebElement(widget, waitS);
    }

    public void waitUntilElementNotVisible(By locator, int waitS) {
        handleSystemAlert();

        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Confirmed not visible: " + locator);
        } catch (TimeoutException e) {
            throw new AssertionError(logError(String.format("Element is still visible: %s - Waited %s seconds", locator, waitS)));
        }
    }

    @Override
    public void waitUntilElementNotVisible(MpaWidget widget, int waitS) {
        handleSystemAlert();

        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));
        By locator;

        try {
            locator = widget.byAndroidResourceId();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byId() - locator: " + locator);
            return;
        } catch (Exception e1) {
            logInfo("Element not found or still visible with widget.byId(), trying next locator...");
        }

        try {
            locator = widget.byAndroidXpathWithResourceId();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byXpathWithId() - locator: " + locator);
            return;
        } catch (Exception e2) {
            logInfo("Element not found or still visible with widget.byXpathWithId(), trying next locator...");
        }

        try {
            locator = By.id(widget.getAndroidId());
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by By.id(widget.getId()) - locator: " + locator);
            return;
        } catch (Exception e3) {
            logInfo("Element not found or still visible with By.id(widget.getId())");
        }
        throw new AssertionError(logError(String.format("Element was found VISIBLE for widget: %s using at least one locator.", widget.getAndroidId())));
    }

    @Override
    public boolean waitUntilElementStaleness(WebElement element, int waitS) {
        handleSystemAlert();

        return waitUntilCondition(ExpectedConditions.stalenessOf(element), waitS);
    }

    @Override
    public WebElement click(By locator) {
        handleSystemAlert();

        WebElement element = this.findElement(locator);

        element.click();

        return element;
    }

    @Override
    public WebElement click(MpaWidget widget) {
        handleSystemAlert();

        WebElement element = getWidgetWebElement(widget);

        element.click();

        return element;
    }

    @Override
    public WebElement safeClick(By locator, int waitS) {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(locator, waitS);

        element.click();

        return element;
    }

    @Override
    public WebElement safeClick(MpaWidget widget, int waitS) {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(widget, waitS);

        element.click();

        return element;
    }

    @Override
    public WebElement sendKeys(By locator, String keys) {
        handleSystemAlert();

        WebElement element = this.findElement(locator);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement sendKeys(MpaWidget widget, String keys) {
        handleSystemAlert();

        WebElement element = getWidgetWebElement(widget);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement safeSendKeys(By locator, int waitS, String keys) {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(locator, waitS);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement safeSendKeys(MpaWidget widget, int waitS, String keys) {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(widget, waitS);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public Color getColor(By locator, int waitS) throws Exception {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(locator, waitS);

        return getColor(element);
    }

    @Override
    public Color getColor(MpaWidget widget, int waitS) throws Exception {
        handleSystemAlert();

        WebElement element = waitUntilElementVisible(widget, waitS);

        return getColor(element);
    }

    @Override
    public Color getColor(WebElement element) throws IOException {
        BufferedImage screenImg = ImageIO.read(getScreenshotAs(OutputType.FILE));
        Rectangle elementRect = element.getRect();

        BufferedImage elementImg = screenImg.getSubimage(elementRect.getX(), elementRect.getY(), elementRect.getWidth(), elementRect.getHeight());
        int centerX = elementImg.getWidth() / 2;
        int centerY = elementImg.getHeight() / 2;
        int rgb = elementImg.getRGB(centerX, centerY);

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        Color c = new Color(red, green, blue);
        logInfo(String.valueOf(rgb));
        logInfo(String.valueOf(c.getAlpha()));
        logInfo(String.valueOf(c.getRed()));
        logInfo(String.valueOf(c.getGreen()));
        logInfo(String.valueOf(c.getBlue()));
        return new Color(red, green, blue);
    }

    @Override
    public boolean matchElementImage(WebElement element, Path referenceImgPath, int waitS) throws IOException {
        //        byte[] screenshot = element.getScreenshotAs(OutputType.BYTES);
        //
        //        Map<String, Object> params = new HashMap<>();
        //        params.put("mode", "matchFeatures");
        //        params.put("referenceImagePath", referenceImgPath.toString());
        //        params.put("screenshot", screenshot);
        //        params.put("goodMatchesFactor", 40);
        //        params.put("matchFunc", "BRUTE_FORCE_HAMMING");
        //
        //        return (boolean) this.executeScript("plugin:images.compareImages", params);

        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));
        wait.until(ExpectedConditions.visibilityOf(element));
        byte[] screenshot = element.getScreenshotAs(OutputType.BYTES);

        FeaturesMatchingResult result = this
                .matchImagesFeatures(
                        screenshot,
                        Files.readAllBytes(referenceImgPath),
                        new FeaturesMatchingOptions()
                                .withDetectorName(FeatureDetector.ORB)
                                .withGoodMatchesFactor(40)
                                .withMatchFunc(MatchingFunction.BRUTE_FORCE_HAMMING)
                                .withEnabledVisualization()
                );

        return result.getCount() > 0;
    }

    @Override
    public Set<String> clickElementsInContainer(
            String elementsWidgetType,
            MpaWidget containerWidget,
            int waitS,
            int maxSelection,
            String scrollDirection
    ) {
        handleSystemAlert();

        WebElement container = this.waitUntilElementVisible(containerWidget.byAndroidResourceId(), waitS);
        Set<String> clickedElementIds = new HashSet<>();
        boolean canScrollMore = true;
        int maxTries = 10;
        int attempt = 0;

        try {
            while (canScrollMore) {
                List<WebElement> visibleElements = container.findElements(By.xpath(".//android.widget." + elementsWidgetType));

                for (WebElement we : visibleElements) {
                    we.click();
                    String elementId = we.getAttribute("resource-id");
                    clickedElementIds.add(elementId);

                    if (clickedElementIds.size() >= maxSelection) {
                        return clickedElementIds;
                    }
                }

                canScrollMore = scrollAll(this.waitUntilElementVisible(containerWidget.byAndroidResourceId(), waitS), scrollDirection);
                attempt++;

                if (attempt > maxTries) {
                    throw new RuntimeException(logError("Too many scrolling attempts, possible infinite loop."));
                }
            }
        } catch (StaleElementReferenceException e) {
            return clickedElementIds;
        }

        return clickedElementIds;
    }

    @Override
    public void swipePercent(WebElement scrollableElement, String direction, int percent) {
    }

    @Override
    public void scrollPercent(WebElement scrollableElement, String direction, int percent) {
        Map<String, Object> scrollArgs = new HashMap<>();
        Rectangle rect = scrollableElement.getRect();

        scrollArgs.put("left", rect.getX());
        scrollArgs.put("top", rect.getY());
        scrollArgs.put("width", rect.getWidth());
        scrollArgs.put("height", rect.getHeight());
        scrollArgs.put("direction", direction);
        scrollArgs.put("percent", percent);
        scrollArgs.put("elementId", ((RemoteWebElement) scrollableElement).getId());

        try {
            this.executeScript("mobile: scrollGesture", scrollArgs);
        } catch (Exception endOfScrollReached) {
            logInfo("Unable to scroll further: " + endOfScrollReached.getMessage());
        }
    }

    @Override
    public boolean scrollAll(WebElement scrollableElement, String direction) {
        handleSystemAlert();

        boolean canScrollMore = true;

        try {
            this.scrollPercent(scrollableElement, direction, 5);
        } catch (Exception e) {
            canScrollMore = false;
        }

        return canScrollMore;
    }

    @Override
    public void scrollToText(WebElement scrollableElement, String direction, String text) {
        handleSystemAlert();

        Map<String, Object> scrollArgs = new HashMap<>();

        scrollArgs.put("strategy", "accessibility id");
        scrollArgs.put("selector", text);
        scrollArgs.put("scrollableElement", null);
        scrollArgs.put("maxSwipes", 10);
        scrollArgs.put("direction", direction);
        scrollArgs.put("elementId", ((RemoteWebElement) scrollableElement).getId());

        try {
            this.executeScript("mobile: scroll", scrollArgs);
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    @Override
    public boolean safeIsSelected(WebElement element) {
        handleSystemAlert();

        return Boolean.parseBoolean(element.getAttribute("checked"));
    }

    @Override
    public String safeGetText(WebElement element) {
        handleSystemAlert();

        String text = element.getText();

        if (text == null || text.isEmpty()) {
            text = element.getAttribute("content-desc");
        }

        return text != null ? text : "";
    }

    @Override
    public boolean isAppRunning(String packageName) {
        try {
            String output = this.executeScript("mobile: shell", Map.of("command", "pidof " + packageName)).toString();
            return output != null && !output.trim().isEmpty();
        } catch (Exception e) {
            logError("Failed to check if app is running: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void terminateMpa(boolean clearAppData) {
        handleSystemAlert();

        installMpa();

        if (clearAppData) {
            executeScript(
                    "mobile: shell",
                    Map.of(
                            "command", "pm",
                            "args", List.of("clear", MPA_APP_ID)
                    )
            );
        } else {
            executeScript("mobile: terminateApp", Map.of("appId", MPA_APP_ID));
        }
    }

    @Override
    public void activateMpa() {
        handleSystemAlert();

        installMpa();

        executeScript("mobile: activateApp", Map.of("appId", MPA_APP_ID));
    }

    @Override
    public void restartMpa(boolean clearAppData, int waitS) {
        handleSystemAlert();

        installMpa();

        boolean wasPrevContextNativeApp = isNativeAppContext();

        try {
            if (this.isAppRunning(MPA_APP_ID)) {
                terminateMpa(clearAppData);

                if (waitUntilCondition(() -> this.isAppRunning(MPA_APP_ID), waitS)) {
                    logError("App termination timeout");
                }
            }

            activateMpa();
            if (!waitUntilCondition(() -> this.isAppRunning(MPA_APP_ID), waitS)) {
                logError("App activation timeout");
            }

            if (wasPrevContextNativeApp && !isNativeAppContext()) {
                this.context("NATIVE_APP");
            }
        } catch (Exception e) {
            throw new RuntimeException(logError("Error during restartMpa: " + e.getMessage()));
        }
    }

    @Override
    public void uninstallMpa() {
        if (this.isAppInstalled(MPA_APP_ID)) {
            this.removeApp(MPA_APP_ID);
            logInfo(String.format("Unistall %s: successful", MPA_APP_ID));
        } else {
            logInfo(String.format("Unistall %s: app not installed", MPA_APP_ID));
        }
    }

    @Override
    public void installMpa() {
        installApp(MPA_APK_DIR, MPA_APP_ID);
    }

    @Override
    public void installApp(Path dotApkPath, String appId) {
        if (!dotApkPath.toFile().exists()) {
            throw new IllegalArgumentException(logError("Source APK file not found at: " + dotApkPath));
        }

        if (!this.isAppInstalled(appId)) {
            this.installApp(dotApkPath.toFile().getAbsolutePath());
            logInfo(String.format("Install app from %s: successful", dotApkPath));
        } else {
            logInfo(String.format("Install app from %s: app already installed", dotApkPath));
        }
    }
}
