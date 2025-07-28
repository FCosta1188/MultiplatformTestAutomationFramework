package eu.nets.test.flows.PinSetup;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.data.PinSetup.PinSetupData;
import eu.nets.test.flows.data.models.MpaUser;
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static eu.nets.test.core.enums.AndroidSnapshot.MPA_LOGGED_OUT;
import static eu.nets.test.core.enums.MpaWidget.ALLOW_NOTIFICATIONS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CONTINUE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.EMAIL_INPUT;
import static eu.nets.test.core.enums.MpaWidget.IOS_UPDATE_APP_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.LOGIN_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OTP_INPUT;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_RULES;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_RULES_TITLE;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_TEXT;
import static eu.nets.test.core.enums.MpaWidget.PIN_TITLE;
import static eu.nets.test.core.enums.MpaWidget.SEE_DATA_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SET_PIN_INPUT;
import static eu.nets.test.util.AllureUtil.logInfo;
import static eu.nets.test.util.MailUtil.getOtp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PinSetupLoginFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;
    private final int WAIT_40_S = 40;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        if (EnvUtil.isAndroid()) {
            return MPA_LOGGED_OUT;
        } else if (EnvUtil.isIos()) {
            return null;
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.PinSetup.PinSetupData#stream")
    @Epic("")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6700")
    @Story("")
    @Description("")
    protected void runTest(
            MpaUser user,
            MpaLanguage appLanguage
    ) {
        run(user, appLanguage);
    }

    public void run(
            MpaUser user,
            MpaLanguage appLanguage
    ) {
        Allure.step("Launch driver and set system language: " + appLanguage, () -> {
            if (appLanguage != null) {
                launchDriver(false, appLanguage.getLanguage(), appLanguage.getCountry());
            } else {
                throw new RuntimeException("Unable to launch driver due to: appLanguage is null");
            }
        });

        Allure.step("Restart MPA - clearAppData: true", () -> {
            driver.restartMpa(true, WAIT_30_S);
        });

        if (EnvUtil.isIos()) {
            Allure.step("[iOS specific]: Allow notifications (if applicable), UPDATE (if applicable)");
            try {
                driver.safeClick(ALLOW_NOTIFICATIONS_BUTTON.byIosXpathWithName(appLanguage.capitalizeDictionaryEntry("allow", false)), WAIT_10_S);
            } catch (Exception skipOptionalElement) {
                logInfo("Element \"" + ALLOW_NOTIFICATIONS_BUTTON + "\" not found -> SKIPPED");
            }

            try {
                WebElement iosUpdateAppButton = driver.findElement(IOS_UPDATE_APP_BUTTON.byIosXpathWithName());
                iosUpdateAppButton.click();
            } catch (NoSuchElementException skipOptionalElement) {
                logInfo("Element \"" + IOS_UPDATE_APP_BUTTON + "\" not found -> SKIPPED");
            }
        }

        Allure.step("Tap LOG IN (if applicable)", () -> {
            try {
                if (EnvUtil.isAndroid()) {
                    driver.safeClick(LOGIN_BUTTON, WAIT_10_S);
                } else if (EnvUtil.isIos()) {
                    driver.safeClick(LOGIN_BUTTON.byIosXpathWithName(appLanguage.capitalizeDictionaryEntry("log in", false)), WAIT_10_S);
                } else {
                    throw new UnsupportedPlatformException();
                }
            } catch (Exception skipElement) {
                logInfo("Element \"" + LOGIN_BUTTON + "\" not found -> SKIPPED");
            }
        });

        Allure.step("Insert username and tap CONTINUE: " + user.email(), () -> {
            WebElement emailInput = driver.safeSendKeys(EMAIL_INPUT, WAIT_10_S, user.email());
            WebElement continueButton = driver.safeClick(CONTINUE_BUTTON, WAIT_10_S);

            //TODO: remove workaround below when fix is released (ISSUE: currently, the email page might reappear after clicking CONTINUE)
            boolean isEmailInputStale = false;
            if(EnvUtil.isAndroid()) {
                isEmailInputStale = driver.waitUntilElementStaleness(emailInput, WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                isEmailInputStale = driver.waitUntilElementStaleness(emailInput, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            if(!isEmailInputStale) {
                logInfo("Elements \"" + EMAIL_INPUT + "and" + CONTINUE_BUTTON + "\" still visible -> RETRYING once");
                driver.safeSendKeys(EMAIL_INPUT, WAIT_10_S, user.email());
                driver.safeClick(CONTINUE_BUTTON, WAIT_10_S);
            }
        });

        Allure.step("Insert OTP", () -> {
            String otp = getOtp(user, GMAIL_APP_PSW, true);

            if (EnvUtil.isAndroid()) {
                driver.safeSendKeys(OTP_INPUT, WAIT_40_S, otp);
            } else if (EnvUtil.isIos()) {
                for (int i = 1; i <= otp.length(); i++) {
                    driver.safeSendKeys(
                            OTP_INPUT.byXpath(
                                    "//XCUIElementTypeApplication[@name=\"MyPayments " +
                                            "Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther" +
                                            "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2" +
                                            "]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextField[" + i + "]"),
                            WAIT_40_S,
                            String.valueOf(otp.charAt(i - 1))
                    );
                }
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Choose PIN: verify \"i\" button and modal", () -> {
            if(EnvUtil.isAndroid()) {
                assertEquals(
                        "Choose PIN",
                        driver.waitUntilElementVisible(PIN_TITLE, WAIT_10_S).getText(),
                        "[Android lockTitle text mismatch]"
                );

                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_RULES_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", PinSetupData.TITLE), WAIT_10_S);
                assertTrue(
                        driver.waitUntilElementVisible(PIN_INFO_RULES, WAIT_10_S).getText().contains(PinSetupData.BULLET_POINT_1),
                        "[Android pinRules text mismatch]"
                );
                assertTrue(
                        driver.waitUntilElementVisible(PIN_INFO_RULES, WAIT_10_S).getText().contains(PinSetupData.BULLET_POINT_2),
                        "[Android pinRules text mismatch]"
                );
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);

                assertEquals(
                        "Choose PIN",
                        driver.waitUntilElementVisible(PIN_TITLE, WAIT_10_S).getText(),
                        "[Android lockTitle text mismatch]"
                );
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Choose PIN"), WAIT_10_S);
                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.TITLE), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_1), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_2), WAIT_10_S);
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Choose PIN"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN);
        });

        EnvUtil.safeSleep(2000);

        Allure.step("Confirm PIN: verify \"i\" button and modal", () -> {
            if(EnvUtil.isAndroid()) {
                driver.waitUntilElementVisible(PIN_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Confirm your code"), WAIT_10_S);

                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_RULES_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", PinSetupData.TITLE), WAIT_10_S);
                assertTrue(
                        driver.waitUntilElementVisible(PIN_INFO_RULES, WAIT_10_S).getText().contains(PinSetupData.BULLET_POINT_1),
                        "[Android pinRules text mismatch]"
                );
                assertTrue(
                        driver.waitUntilElementVisible(PIN_INFO_RULES, WAIT_10_S).getText().contains(PinSetupData.BULLET_POINT_2),
                        "[Android pinRules text mismatch]"
                );
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);

                driver.waitUntilElementVisible(PIN_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Confirm your code"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Please confirm the new PIN"), WAIT_10_S);
                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.TITLE), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_1), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_2), WAIT_10_S);
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Please confirm the new PIN"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN);
        });

        Allure.step("Land on Select Sales Locations page", () -> {
            driver.waitUntilElementVisible(SEE_DATA_BUTTON, WAIT_10_S);
        });
    }
}
