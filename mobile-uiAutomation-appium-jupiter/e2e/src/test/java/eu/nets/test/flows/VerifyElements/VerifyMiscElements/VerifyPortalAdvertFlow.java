package eu.nets.test.flows.VerifyElements.VerifyMiscElements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.drivers.MpaAndroidDriver;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.VerifyElements.VerifyMiscElements.VerifyPortalAdvertData;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_GET_IN_CONTACT_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_GET_IN_CONTACT_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_WEBVIEW_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_WEBVIEW_BACK_BUTTON_IOS_BUG;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_WEBVIEW_LOGO_NEXI;
import static eu.nets.test.core.enums.MpaWidget.SETTLEMENTS_WEBVIEW_NETS_ACCOUNT;
import static eu.nets.test.util.AllureUtil.logError;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyPortalAdvertFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    private WebElement getInContactButton;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifyMiscElements.VerifyPortalAdvertData#stream")
    @Epic("")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6666")
    @Story("")
    @Description("")
    protected void runTest(
            MpaUser user,
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        run(user, appLanguage);
    }

    protected void run(
            MpaUser user,
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        if (EnvUtil.isAndroid() && user.loggedInAndroidSnapshot().exists()) {
            Allure.step("[Android specific]: load logged in snapshot: " + user.loggedInAndroidSnapshot(), () -> {
                user.loggedInAndroidSnapshot().load();
            });
            Allure.step("Launch driver and set system language: " + appLanguage, () -> {
                if (appLanguage != null) {
                    launchDriver(false, appLanguage.getLanguage(), appLanguage.getCountry());
                } else {
                    throw new RuntimeException("Unable to launch driver due to: appLanguage is null");
                }
            });
        } else if ((EnvUtil.isAndroid() && !user.loggedInAndroidSnapshot().exists()) || EnvUtil.isIos()) {
            Allure.step("run RegistrationFlow - user: " + user, () -> {
                RegistrationFlow rFlow = new RegistrationFlow();
                rFlow.setDriver(this.driver);
                rFlow.run(user, appLanguage, false, false);
                this.setDriver(rFlow.getDriver());
            });
        }

        Allure.step("Open OVERVIEW -> Accounting -> Settlements tab (selected by default) -> GET IN CONTACT -> WebView", () -> {
            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                getInContactButton = driver.safeClick(SETTLEMENTS_GET_IN_CONTACT_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//android.webkit.WebView"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                getInContactButton = driver.safeClick(SETTLEMENTS_GET_IN_CONTACT_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeWebView"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Verify WebView URL and/or elements", () -> {
            EnvUtil.safeSleep(5000);

            if(EnvUtil.isAndroid()) {
                driver.switchAppContext("WEBVIEW_" + MpaAndroidDriver.MPA_APP_ID);
                assertTrue(
                        driver.getCurrentUrl().contains(VerifyPortalAdvertData.baseAuthUrl(user.type())),
                        "[WebView URL contains() mismatch]" +
                                "\n\tExpected: " + VerifyPortalAdvertData.baseAuthUrl(user.type()) +
                                "\n\tActual  : " + driver.getCurrentUrl()
                );
                driver.switchAppContext("NATIVE_APP");
            } else if (EnvUtil.isIos()) {
                switch (user.type()) {
                    case "SMP":
                    case "BAU":
                        driver.waitUntilElementVisible(SETTLEMENTS_WEBVIEW_LOGO_NEXI, WAIT_10_S);
                        break;
                    case "TNP":
                        //TODO https://nexigroup-germany.atlassian.net/browse/MSA-6666
                        break;
                    case "PUMA":
                    case "MEPO":
                        driver.waitUntilElementVisible(SETTLEMENTS_WEBVIEW_NETS_ACCOUNT, WAIT_10_S);
                        break;

                    default:
                        throw new IllegalArgumentException(logError("Invalid user type: " + user.type()));
                }
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Close WebView, close GET IN CONTACT card, restart app and verify GET IN CONTACT not present", () -> {
            try {
                driver.safeClick(SETTLEMENTS_WEBVIEW_BACK_BUTTON, WAIT_10_S);
            } catch (NoSuchElementException elementNotFound) {
                driver.safeClick(SETTLEMENTS_WEBVIEW_BACK_BUTTON_IOS_BUG, WAIT_10_S);
            }
            driver.safeClick(SETTLEMENTS_GET_IN_CONTACT_CLOSE_BUTTON, WAIT_10_S);
            driver.waitUntilElementStaleness(getInContactButton, WAIT_10_S);

            driver.restartMpa(false, WAIT_30_S);
            if (EnvUtil.isAndroid()) {
                driver.safeSendKeys(PIN_INPUT, WAIT_10_S, APP_PIN);
            } else if (EnvUtil.isIos()) {
                for (int i = 1; i <= APP_PIN.length(); i++) {
                    driver.safeSendKeys(
                            PIN_INPUT.byXpath(
                                "//XCUIElementTypeApplication[@name=\"MyPayments Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeSecureTextField[" + i + "]"
                            ),
                            WAIT_10_S,
                            String.valueOf(APP_PIN.charAt(i - 1))
                    );
                }
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.waitUntilElementStaleness(getInContactButton, WAIT_10_S);
        });

        Allure.step("run RegistrationFlow - user: " + user, () -> {
            RegistrationFlow rFlow = new RegistrationFlow();
            rFlow.setDriver(this.driver);
            rFlow.run(user, appLanguage, false, false);
            this.setDriver(rFlow.getDriver());
        });

        Allure.step("Verify GET IN CONTACT reappears after Log out and Log in", () -> {
            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(SETTLEMENTS_GET_IN_CONTACT_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "GET IN CONTACT"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(SETTLEMENTS_GET_IN_CONTACT_BUTTON, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
