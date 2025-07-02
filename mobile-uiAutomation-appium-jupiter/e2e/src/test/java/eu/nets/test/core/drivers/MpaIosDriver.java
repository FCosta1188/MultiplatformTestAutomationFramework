package eu.nets.test.core.drivers;

import eu.nets.test.core.enums.MpaWidget;
import eu.nets.test.core.enums.PathKey;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.imagecomparison.FeatureDetector;
import io.appium.java_client.imagecomparison.FeaturesMatchingOptions;
import io.appium.java_client.imagecomparison.FeaturesMatchingResult;
import io.appium.java_client.imagecomparison.MatchingFunction;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;

public class MpaIosDriver extends IOSDriver implements MpaDriver, TestWatcher {
    public static final String MPA_BUNDLE_ID = PropertiesUtil.ENV.getProperty("mpaBundleId"); // bundle ID
    public static final Path MPA_APP_DIR = PathKey.MPA.resolve().asPath();
    public static final int SLEEP_MS = 1000;

    public MpaIosDriver() throws MalformedURLException {
        super(
                new URL(PropertiesUtil.CONFIG.getProperty("appium.driver.remoteAddress")),
                EnvUtil.getXCUITestOptions()
        );
    }

    public MpaIosDriver(int waitS, boolean setAppiumApp) throws MalformedURLException {
        super(
                new URL(PropertiesUtil.CONFIG.getProperty("appium.driver.remoteAddress")),
                EnvUtil.getDesideredCapabilities(waitS, setAppiumApp)
        );
    }

    public MpaIosDriver(int waitS, boolean setAppiumApp, String language, String locale) throws MalformedURLException {
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
    public void handleSystemAlert() {
    }

    @Override
    public WebElement getWidgetWebElement(MpaWidget widget) {
        WebElement element;
        By locator;
        try {
            locator = widget.byIosAccessibilityId();
            element = this.findElement(locator);
            logInfo("Element found by widget.byIosAccessibilityId()) - locator: " + locator);
        } catch (Exception e1) {
            try {
                locator = widget.byIosXpathWithName();
                element = this.findElement(locator);
                logInfo("Element found by widget.byIosXpathWithName() - locator: " + locator);
            } catch (Exception e2) {
                try {
                    locator = widget.byIosXpathWithValue();
                    element = this.findElement(locator);
                    logInfo("Element found by widget.byIosXpathWithValue() - locator: " + locator);
                } catch (Exception e3) {
                    try {
                        locator = widget.byIosXpathWithLabel();
                        element = this.findElement(locator);
                        logInfo("Element found by widget.byIosXpathWithLabel() - locator: " + locator);
                    } catch (Exception e4) {
                        throw new RuntimeException(
                                logError(
                                        String.format(
                                                "Element not found: %s - Exception: %s",
                                                widget.getIosAccessibilityId(),
                                                e4.getMessage()
                                        )
                                )
                        );
                    }
                }
            }
        }

        return element;
    }

    @Override
    public WebElement safeGetWidgetWebElement(MpaWidget widget, int waitS) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        WebElement element;
        By locator;
        try {
            locator = widget.byIosAccessibilityId();
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logInfo("Element found by widget.byIosAccessibilityId()) - locator: " + locator);
        } catch (Exception e1) {
            try {
                locator = widget.byIosXpathWithName();
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                logInfo("Element found by widget.byIosXpathWithName() - locator: " + locator);
            } catch (Exception e2) {
                try {
                    locator = widget.byIosXpathWithValue();
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    logInfo("Element found by widget.byIosXpathWithValue() - locator: " + locator);
                } catch (Exception e3) {
                    try {
                        locator = widget.byIosXpathWithLabel();
                        element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                        logInfo("Element found by widget.byIosXpathWithLabel() - locator: " + locator);
                    } catch (Exception e4) {
                        throw new RuntimeException(
                                logError(
                                        String.format(
                                                "Element not found: %s - Exception: %s",
                                                widget.getIosAccessibilityId(),
                                                e4.getMessage()
                                        )
                                )
                        );
                    }
                }
            }
        }

        return element;
    }

    @Override
    public WebElement waitUntilElementVisible(By locator, int waitS) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        WebElement element;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logInfo("Element found by locator: " + locator);
        } catch (Exception e) {
            throw new RuntimeException(logError(String.format("Element not found: %s - Waited %s seconds (polling every %s ms) - Exception: %s",
                    locator.toString(),
                    waitS,
                    SLEEP_MS,
                    e.getMessage()
            )));
        }

        return element;
    }

    @Override
    public WebElement waitUntilElementVisible(MpaWidget widget, int waitS) {
        return safeGetWidgetWebElement(widget, waitS);
    }

    @Override
    public void waitUntilElementNotVisible(By locator, int waitS) {
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
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(waitS), Duration.ofMillis(SLEEP_MS));

        By locator;
        try {
            locator = widget.byIosAccessibilityId();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byIosAccessibilityId() - locator: " + locator);
            return;
        } catch (Exception e1) {
            logInfo("Element not found or still visible with widget.byIosAccessibilityId(), trying next locator...");
        }

        try {
            locator = widget.byIosXpathWithName();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byIosXpathWithName() - locator: " + locator);
            return;
        } catch (Exception e2) {
            logInfo("Element not found or still visible with widget.byIosXpathWithName(), trying next locator...");
        }

        try {
            locator = widget.byIosXpathWithValue();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byIosXpathWithValue() - locator: " + locator);
            return;
        } catch (Exception e3) {
            logInfo("Element not found or still visible with widget.byIosXpathWithValue(), trying next locator...");
        }

        try {
            locator = widget.byIosXpathWithLabel();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logInfo("Element not visible by widget.byIosXpathWithLabel() - locator: " + locator);
            return;
        } catch (Exception e4) {
            logInfo("Element not found or still visible with widget.byIosXpathWithLabel()");
        }

        throw new AssertionError(logError(String.format("Element was found VISIBLE for widget: %s using at least one locator.", widget)));
    }

    @Override
    public boolean waitUntilElementStaleness(WebElement element, int waitS) {
        return waitUntilCondition(ExpectedConditions.stalenessOf(element), waitS);
    }

    @Override
    public WebElement click(By locator) {
        WebElement element = this.findElement(locator);

        element.click();

        return element;
    }

    @Override
    public WebElement click(MpaWidget widget) {
        WebElement element = this.getWidgetWebElement(widget);

        element.click();

        return element;
    }

    @Override
    public WebElement safeClick(By locator, int waitS) {
        WebElement element = waitUntilElementVisible(locator, waitS);

        element.click();

        return element;
    }

    @Override
    public WebElement safeClick(MpaWidget widget, int waitS) {
        WebElement element = waitUntilElementVisible(widget, waitS);

        element.click();

        return element;
    }

    @Override
    public WebElement sendKeys(By locator, String keys) {
        WebElement element = this.findElement(locator);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement sendKeys(MpaWidget widget, String keys) {
        WebElement element = getWidgetWebElement(widget);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement safeSendKeys(By locator, int waitS, String keys) {
        WebElement element = waitUntilElementVisible(locator, waitS);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public WebElement safeSendKeys(MpaWidget widget, int waitS, String keys) {
        WebElement element = waitUntilElementVisible(widget, waitS);

        element.sendKeys(keys);

        return element;
    }

    @Override
    public Color getColor(By locator, int waitS) throws Exception {
        WebElement element = waitUntilElementVisible(locator, waitS);

        return getColor(element);
    }

    @Override
    public Color getColor(MpaWidget widget, int waitS) throws Exception {
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
        WebElement container = this.waitUntilElementVisible(containerWidget.byAndroidResourceId(), waitS);
        Set<String> clickedElementIds = new HashSet<>();
        boolean canScrollMore = true;
        int maxTries = 10;
        int attempt = 0;

        try {
            while (canScrollMore) {
                List<WebElement> visibleElements = container.findElements(By.xpath(".//XCUIElementType" + elementsWidgetType));

                for (WebElement we : visibleElements) {
                    String elementId = we.getAttribute("elementId");
                    if (!we.isSelected() && !clickedElementIds.contains(elementId)) {
                        we.click();
                    }
                    clickedElementIds.add(elementId);

                    if (clickedElementIds.size() >= maxSelection) {
                        return clickedElementIds;
                    }
                }

                canScrollMore = scrollAll(this.waitUntilElementVisible(containerWidget, waitS), scrollDirection);
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
        Rectangle rect = scrollableElement.getRect();
        double scrollFactor = Math.min(Math.max(percent, 1), 100) / 100.0;

        int width = rect.getWidth();
        int height = rect.getHeight();
        int startX, startY, endX, endY;

        switch (direction.toLowerCase()) {
            case "up":
                startX = rect.getX() + width / 2;
                startY = rect.getY() + (int) (height * 0.8);
                endX = startX;
                endY = startY - (int) (height * scrollFactor);
                break;
            case "down":
                startX = rect.getX() + width / 2;
                startY = rect.getY() + (int) (height * 0.2);
                endX = startX;
                endY = startY + (int) (height * scrollFactor);
                break;
            case "left":
                startY = rect.getY() + height / 2;
                startX = rect.getX() + (int) (width * 0.8);
                endY = startY;
                endX = startX - (int) (width * scrollFactor);
                break;
            case "right":
                startY = rect.getY() + height / 2;
                startX = rect.getX() + (int) (width * 0.2);
                endY = startY;
                endX = startX + (int) (width * scrollFactor);
                break;
            default:
                throw new IllegalArgumentException(logError("Invalid direction: " + direction));
        }

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        try {
            this.perform(Collections.singletonList(swipe));
        } catch (Exception e) {
            System.err.println("Errore durante swipe W3C: " + e.getMessage());
        }
    }

    @Override
    public void scrollPercent(WebElement scrollableElement, String direction, int percent) {
    }

    @Override
    public boolean scrollAll(WebElement scrollableElement, String direction) {
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
            logError("Unable to execute script \"mobile: scroll\": " + e.getMessage());
        }
    }

    @Override
    public boolean safeIsSelected(WebElement element) {
        return Boolean.parseBoolean(element.getAttribute("checked"));
    }

    @Override
    public String safeGetText(WebElement element) {
        String text = element.getText();

        if (text == null || text.isEmpty()) {
            text = element.getAttribute("content-desc");
        }

        return text != null ? text : "";
    }

    @Override
    public boolean isAppRunning(String bundleId) {
        try {
            ApplicationState appState = this.queryAppState(bundleId);
            return appState == ApplicationState.RUNNING_IN_FOREGROUND ||
                    appState == ApplicationState.RUNNING_IN_BACKGROUND ||
                    appState == ApplicationState.RUNNING_IN_BACKGROUND_SUSPENDED;
        } catch (Exception e) {
            logError("Failed to check if app is running: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void terminateMpa(boolean clearAppData) {
        executeScript("mobile: terminateApp", Map.of("bundleId", MPA_BUNDLE_ID));

        if (clearAppData) {
            uninstallMpa();
            installMpa();
        }
    }

    @Override
    public void activateMpa() {
        executeScript("mobile: activateApp", Map.of("bundleId", MPA_BUNDLE_ID));
    }

    @Override
    public void restartMpa(boolean clearAppData, int waitS) {
        boolean wasPrevContextNativeApp = isNativeAppContext();

        try {
            terminateMpa(clearAppData);
            if (waitUntilCondition(() -> this.isAppRunning(MPA_BUNDLE_ID), waitS)) {
                logError("App termination timeout");
            }

            activateMpa();
            if (!waitUntilCondition(() -> this.isAppRunning(MPA_BUNDLE_ID), waitS)) {
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
        if (this.isAppInstalled(MPA_BUNDLE_ID)) {
            this.removeApp(MPA_BUNDLE_ID);
            logInfo(String.format("Unistall %s: successful", MPA_BUNDLE_ID));
        } else {
            logInfo(String.format("Unistall %s: app not installed", MPA_BUNDLE_ID));
        }
    }

    @Override
    public void installMpa() {
        installApp(MPA_APP_DIR, MPA_BUNDLE_ID);
    }

    @Override
    public void installApp(Path dotAppPath, String bundleId) {
        if (!dotAppPath.toFile().exists()) {
            throw new IllegalArgumentException(logError("Source APP file not found at: " + dotAppPath));
        }

        if (!this.isAppInstalled(bundleId)) {
            this.installApp(dotAppPath.toFile().getAbsolutePath());
            logInfo(String.format("Install app from %s: successful", dotAppPath));
        } else {
            logInfo(String.format("Install app from %s: app already installed", dotAppPath));
        }
    }
}

