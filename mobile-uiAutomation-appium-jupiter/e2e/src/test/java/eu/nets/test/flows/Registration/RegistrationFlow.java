package eu.nets.test.flows.Registration;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
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
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static eu.nets.test.core.enums.AndroidSnapshot.MPA_LOGGED_OUT;
import static eu.nets.test.core.enums.AndroidSnapshot.NO_MPA;
import static eu.nets.test.core.enums.MpaWidget.ACCEPT_TERMS_SWITCH;
import static eu.nets.test.core.enums.MpaWidget.ALLOW_NOTIFICATIONS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CODE_SENT_OK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CONTINUE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.EMAIL_INPUT;
import static eu.nets.test.core.enums.MpaWidget.IOS_UPDATE_APP_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.LOGIN_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OTP_INPUT;
import static eu.nets.test.core.enums.MpaWidget.PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.RESEND_CODE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SECTION_TITLE;
import static eu.nets.test.core.enums.MpaWidget.SEE_DATA_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SELECT_ALL_LOCATIONS_CHECKBOX;
import static eu.nets.test.core.enums.MpaWidget.SET_PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.SKIP_GET_STARTED_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.TERMINAL_SETUP_SKIP_BUTTON;
import static eu.nets.test.util.AllureUtil.logInfo;
import static eu.nets.test.util.MailUtil.getOtp;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class  RegistrationFlow extends AbstractFlow {
    private final int WAIT_5_S = 5;
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;
    private final int WAIT_40_S = 40;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        if (EnvUtil.isAndroid()) {
            return NO_MPA;
        } else if (EnvUtil.isIos()) {
            return null;
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.Registration.RegistrationData#stream")
    @Epic("Registration")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6118")
    @Story("First login with different types of credentials")
    @Description("Login to the app for the first time, setup a PIN and land on Overview")
    protected void runTest(
            MpaUser user,
            MpaLanguage appLanguage
    ) {
        run(user, appLanguage, true, false);
    }

    public void run(
            MpaUser user,
            MpaLanguage appLanguage,
            boolean testInAppPin,
            boolean testResendOtp
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

        Allure.step("Insert OTP - test resend code: " + testResendOtp, () -> {
            String otp = getOtp(user, GMAIL_APP_PSW, true);

            if (testResendOtp) {
                driver.safeClick(RESEND_CODE_BUTTON, WAIT_10_S);
                otp = getOtp(user, GMAIL_APP_PSW, true);
                driver.safeClick(CODE_SENT_OK_BUTTON, WAIT_10_S);
            }

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

        Allure.step("Choose PIN", () -> {
            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN);
        });

        Allure.step("Confirm PIN", () -> {
            EnvUtil.safeSleep(2000);
            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN);
        });

        Allure.step("Select location(s), accept terms and tap SEE DATA: " + user.org(), () -> {
            //TODO: implement multiple checkbox selection logic (driver.clickElementsInContainer) -> remove workarounds below

            WebElement selectAllLocationsCheckbox = null;
            boolean selectAllLocationsChecked = false;
            try {
                selectAllLocationsCheckbox = driver.waitUntilElementVisible(SELECT_ALL_LOCATIONS_CHECKBOX, WAIT_10_S);
                selectAllLocationsChecked = true;
            } catch (Exception skipElement) {
                logInfo("Element \"" + SELECT_ALL_LOCATIONS_CHECKBOX + "\" not found -> SKIPPED");
            }

            List<WebElement> visibleCheckboxes;
            if (EnvUtil.isAndroid()) {
                visibleCheckboxes = driver.findElements(By.xpath(".//android.widget.CheckBox"));
            } else if (EnvUtil.isIos()) {
                visibleCheckboxes = driver.findElements(By.xpath(".//XCUIElementTypeCell"));
            } else {
                throw new UnsupportedPlatformException();
            }

            if (selectAllLocationsChecked) {
                if (EnvUtil.isIos() && visibleCheckboxes.size() >= 12) {
                    //workaround: select 1st location only
                    driver.safeClick(By.xpath("//XCUIElementTypeTable[@name=\"setup_table_view\"]/XCUIElementTypeCell[1]"), WAIT_10_S);
                    //after refactor: driver.safeClick(By.xpath("(//XCUIElementTypeCell[@name=\"CollapsableTableViewCell\"])[1]"), WAIT_10_S);
                } else {
                    selectAllLocationsCheckbox.click();
                }
            } else {
                if (visibleCheckboxes.isEmpty()) {
                    throw new NoSuchElementException("No outlets found for org: " + user.org());
                } else if (visibleCheckboxes.size() == 1) {
                    if (EnvUtil.isAndroid()) {
                        visibleCheckboxes.get(0).click();
                    }
                    //if iOS, it is already checked by default
                } else {
                    //TODO: implement checkbox selection and scrolling logic to select multiple locations (driver.clickElementsInContainer) -> remove
                    // workaround below
                    if (EnvUtil.isAndroid()) {
                        //workaround: select 1st location only
                        visibleCheckboxes.get(0).click();
                    } else if (EnvUtil.isIos()) {
                        //workaround: select 1st location only
                        driver.safeClick(By.xpath("//XCUIElementTypeTable[@name=\"setup_table_view\"]/XCUIElementTypeCell[1]"), WAIT_10_S);
                        //after refactor: driver.safeClick(By.xpath("(//XCUIElementTypeCell[@name=\"CollapsableTableViewCell\"])[1]"), WAIT_10_S);
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                }
            }

            driver.safeClick(ACCEPT_TERMS_SWITCH, WAIT_10_S);
            driver.safeClick(SEE_DATA_BUTTON, WAIT_10_S);
        });

        // Skipping this steps if the user is Swiss
        if (!user.type().equals("SMP_CH")) {
            Allure.step("Tap I DON'T NEED TO SETUP TERMINALS (if applicable)", () -> {

                try {
                    driver.safeClick(TERMINAL_SETUP_SKIP_BUTTON, WAIT_10_S);
                } catch (Exception skipElement) {
                    logInfo("Element \"" + TERMINAL_SETUP_SKIP_BUTTON + "\" not found -> SKIPPED");
                }
            });

            Allure.step("Skip Get Started (if applicable)", () -> {
                try {
                    driver.safeClick(SKIP_GET_STARTED_CLOSE_BUTTON, WAIT_10_S);
                } catch (Exception skipElement) {
                    logInfo("Element \"" + SKIP_GET_STARTED_CLOSE_BUTTON + "\" not found -> SKIPPED");
                }
            });
        }

        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: Allow notifications (if applicable)", () -> {
                try {
                    driver.safeClick(ALLOW_NOTIFICATIONS_BUTTON, WAIT_10_S);
                } catch (Exception skipElement) {
                    logInfo("Element \"" + ALLOW_NOTIFICATIONS_BUTTON + "\" not found -> SKIPPED");
                }
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

        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: save snapshot: " + user.loggedInAndroidSnapshot(), () -> {
                if (!user.loggedInAndroidSnapshot().exists()) {
                    user.loggedInAndroidSnapshot().save();
                }
            });
        }
    }
}
