package eu.nets.test.flows.Navigation;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.shared.UserData;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static eu.nets.test.core.enums.AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP;
import static eu.nets.test.core.enums.MpaWidget.ACTION_SUPPORT;
import static eu.nets.test.core.enums.MpaWidget.CALL_US_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CHAT_WITH_US_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.NAV_BAR_ACTIVE_ITEM;
import static eu.nets.test.core.enums.MpaWidget.NAV_BAR_INACTIVE_ITEM;
import static eu.nets.test.core.enums.MpaWidget.PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.SECTION_TITLE;
import static eu.nets.test.core.enums.MpaWidget.SUPPORT_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.TITLE;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NavigationToSupportFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 0;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        if (EnvUtil.isAndroid()) {
            return MPA_LOGGED_IN_SMP_12PP;
        } else if (EnvUtil.isIos()) {
            return null;
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("eu.nets.test.flows.data.shared.LanguageData#streamSupportedByMpa")
    @Epic("Navigation to Support")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6203")
    @Story("Navigate to Support with SMP user")
    @Description("Navigate to the Support page using the navigation bar at the bottom of the screen")
    protected void runTest(
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        run(appLanguage, true);
    }

    protected void run(
            MpaLanguage appLanguage,
            boolean testInAppPin
    ) throws IOException, InterruptedException {
        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: load startup snapshot: " + startupAndroidSnapshot(), () -> {
                if (startupAndroidSnapshot().exists()) {
                    startupAndroidSnapshot().load();
                } else {
                    throw new RuntimeException(logError("Required snapshot does not exist: " + startupAndroidSnapshot() + ". Please create it before running " +
                            "tests."));
                }
            });
        }

        Allure.step("Launch driver and set system language: " + appLanguage, () -> {
            if (appLanguage != null) {
                launchDriver(false, appLanguage.getLanguage(), appLanguage.getCountry());
            }
        });

        if (EnvUtil.isIos()) {
            Allure.step("[iOS specific]: run RegistrationFlow", () -> {
                RegistrationFlow rFlow = new RegistrationFlow();
                rFlow.setDriver(this.driver);
                rFlow.run(UserData.allUsers().get(0), appLanguage, false, false);
                this.setDriver(rFlow.getDriver());
            });
        }

        Allure.step("Land on Overview - test in-app PIN: " + testInAppPin, () -> {
            if (EnvUtil.isAndroid()) {
                assert driver.waitUntilElementVisible(SECTION_TITLE, WAIT_10_S).getText().equals("MyPayments");
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(SECTION_TITLE.byIosXpathWithName("MyPayments"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            if (testInAppPin) {
                driver.restartMpa(false, WAIT_30_S);

                if (EnvUtil.isAndroid()) {
                    driver.safeSendKeys(PIN_INPUT, WAIT_10_S, APP_PIN);
                    assert driver.waitUntilElementVisible(SECTION_TITLE, WAIT_10_S).getText().equals("MyPayments");
                } else if (EnvUtil.isIos()) {
                    for (int i = 1; i <= APP_PIN.length(); i++) {
                        driver.safeSendKeys(
                                PIN_INPUT.byXpath(
                                        "//XCUIElementTypeApplication[@name=\"MyPayments " +
                                                "Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther" +
                                                "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther" +
                                                "/XCUIElementTypeSecureTextField[" + i + "]"),
                                WAIT_10_S,
                                String.valueOf(APP_PIN.charAt(i - 1))
                        );
                    }
                    driver.waitUntilElementVisible(SECTION_TITLE.byIosXpathWithName("MyPayments"), WAIT_10_S);
                } else {
                    throw new UnsupportedPlatformException();
                }
            }
        });

        Allure.step("Tap SUPPORT button and verify Support page layout", () -> {
            driver.safeClick(SUPPORT_DASHBOARD_BUTTON, WAIT_10_S);

            if (EnvUtil.isAndroid()) {
                driver.waitUntilElementVisible(NAV_BAR_ACTIVE_ITEM.byAndroidXpathWithResourceIdAndAttribute("text", "SUPPORT"), WAIT_10_S);
                driver.waitUntilElementVisible(SECTION_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Support"), WAIT_10_S);
                driver.waitUntilElementVisible(CALL_US_BUTTON.byAndroidXpathWithResourceIdAndAttribute("content-desc", "notification_icon_content_desc"),
                        WAIT_10_S);
                // driver.wait(CHAT_WITH_US_BUTTON.byXpathWithIdAndAttribute("content-desc", "menu_icon_content_desc"), WAIT_10_S);
                driver.waitUntilElementVisible(TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Good to know"), WAIT_10_S);
                driver.waitUntilElementVisible(ACTION_SUPPORT.byAndroidXpathWithResourceIdAndAttribute("text", "All support articles"), WAIT_10_S);
                driver.waitUntilElementVisible(ACTION_SUPPORT.byAndroidXpathWithResourceIdAndAttribute("text", "FAQ"), WAIT_10_S);
                driver.waitUntilElementVisible(NAV_BAR_INACTIVE_ITEM.byAndroidXpathWithResourceIdAndAttribute("text", "OVERVIEW"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(SECTION_TITLE.byIosXpathWithName("Support"), WAIT_10_S);
                driver.waitUntilElementVisible(CALL_US_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(CHAT_WITH_US_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(TITLE.byIosXpathWithName("Good to know"), WAIT_10_S);
                driver.waitUntilElementVisible(ACTION_SUPPORT.byIosXpathWithName("All support articles"), WAIT_10_S);
                driver.waitUntilElementVisible(ACTION_SUPPORT.byIosXpathWithName("FAQ"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
