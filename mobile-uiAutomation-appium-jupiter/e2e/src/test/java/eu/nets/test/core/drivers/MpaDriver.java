package eu.nets.test.core.drivers;

import eu.nets.test.core.enums.MpaWidget;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;

public interface MpaDriver extends WebDriver {
    boolean isNativeAppContext();

    boolean waitUntilCondition(Supplier<Boolean> condition, int waitS);

    boolean waitUntilCondition(ExpectedCondition<Boolean> condition, int waitS);

    void handleSystemAlert();

    WebElement getWidgetWebElement(MpaWidget widget);

    WebElement safeGetWidgetWebElement(MpaWidget widget, int waitS);

    WebElement waitUntilElementVisible(By locator, int waitS);

    WebElement waitUntilElementVisible(MpaWidget widget, int waitS);

    void waitUntilElementNotVisible(By locator, int waitS);

    void waitUntilElementNotVisible(MpaWidget widget, int waitS);

    boolean waitUntilElementStaleness(WebElement element, int waitS);

    WebElement click(By locator);

    WebElement click(MpaWidget widget);

    WebElement safeClick(By locator, int waitS);

    WebElement safeClick(MpaWidget widget, int waitS);

    WebElement sendKeys(By locator, String keys);

    WebElement sendKeys(MpaWidget widget, String keys);

    WebElement safeSendKeys(By locator, int waitS, String keys);

    WebElement safeSendKeys(MpaWidget widget, int waitS, String keys);

    Color getColor(By locator, int waitS) throws Exception;

    Color getColor(MpaWidget widget, int waitS) throws Exception;

    Color getColor(WebElement element) throws IOException;

    boolean matchElementImage(WebElement element, Path referenceImgPath, int waitS) throws IOException;

    Set<String> clickElementsInContainer(
            String elementsWidgetType,
            MpaWidget containerWidget,
            int waitS,
            int maxSelection,
            String scrollDirection
    );

    /* --- Scroll VS Swipe ---
     * Scroll down: text scrolls down
     * Swipe down: finger goes down, text scrolls up
     */
    void swipePercent(WebElement scrollableElement, String direction, int percent);

    void scrollPercent(WebElement scrollableElement, String direction, int percent);

    boolean scrollAll(WebElement scrollableElement, String direction);

    void scrollToText(WebElement scrollableElement, String direction, String text);

    boolean safeIsSelected(WebElement element);

    String safeGetText(WebElement element);

    boolean isAppRunning(String packageName);

    void terminateMpa(boolean clearAppData);

    void activateMpa();

    void restartMpa(boolean clearAppData, int waitS);

    void uninstallMpa();

    void installMpa();

    void installApp(Path apkPath, String appPackage);
}