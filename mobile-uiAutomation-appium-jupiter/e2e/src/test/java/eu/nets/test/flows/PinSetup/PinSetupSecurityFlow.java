package eu.nets.test.flows.PinSetup;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
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
import org.openqa.selenium.By;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.CHANGE_CODE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.FORGOT_CODE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_DESCRIPTION;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_RULES;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_RULES_TITLE;
import static eu.nets.test.core.enums.MpaWidget.PIN_INFO_TEXT;
import static eu.nets.test.core.enums.MpaWidget.PIN_TITLE;
import static eu.nets.test.core.enums.MpaWidget.PROFILE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SECURITY_TITLE;
import static eu.nets.test.core.enums.MpaWidget.SET_PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.SIDEMENU_SECURITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PinSetupSecurityFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.PinSetup.PinSetupData#stream")
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

        Allure.step("Open Change Code page", () -> {
            driver.safeClick(PROFILE_BUTTON, WAIT_10_S);
            driver.safeClick(SIDEMENU_SECURITY, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.waitUntilElementVisible(SECURITY_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Security"), WAIT_10_S);
                driver.safeClick(CHANGE_CODE_BUTTON.byAndroidXpathWithAttribute("text", "Change code"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(SECURITY_TITLE, WAIT_10_S);
                driver.safeClick(CHANGE_CODE_BUTTON, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Current PIN: verify \"i\" button and modal, back button", () -> {
            if(EnvUtil.isAndroid()) {
                assertEquals(
                        "Please enter your current code",
                        driver.waitUntilElementVisible(PIN_DESCRIPTION, WAIT_10_S).getText(),
                        "[Android lockDescription text mismatch]"
                );

                driver.waitUntilElementVisible(FORGOT_CODE_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_BACK_BUTTON.byAndroidXpathWithAttribute("content-desc", "Navigate up"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Please enter your current code"), WAIT_10_S);
                //TODO: iOS "forgot code" button not visible in DOM
                //driver.waitUntilElementVisible(FORGOT_CODE_BUTTON.byIosXpathWithName(), WAIT_10_S);

                //back button
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeNavigationBar[@name=\"Change code\"]/XCUIElementTypeButton[1]"), WAIT_10_S);

                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.TITLE), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_1), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_2), WAIT_10_S);
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);

                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Please enter your current code"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN);
        });

        Allure.step("New PIN: verify \"i\" button and modal, back button", () -> {
            if(EnvUtil.isAndroid()) {
                assertEquals(
                        "Enter your new code",
                        driver.waitUntilElementVisible(PIN_TITLE, WAIT_10_S).getText(),
                        "[Android lockTitle text mismatch]"
                );

                driver.waitUntilElementVisible(PIN_BACK_BUTTON.byAndroidXpathWithAttribute("content-desc", "Navigate up"), WAIT_10_S);

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
                        "Enter your new code",
                        driver.waitUntilElementVisible(PIN_TITLE, WAIT_10_S).getText(),
                        "[Android lockTitle text mismatch]"
                );
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Enter your new code"), WAIT_10_S);

                //back button
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeNavigationBar[@name=\"Change code\"]/XCUIElementTypeButton[1]"), WAIT_10_S);

                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.TITLE), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_1), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_2), WAIT_10_S);
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);

                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Enter your new code"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN_CHANGE);
        });

        Allure.step("Confirm New PIN (after change): verify \"i\" button and modal, back button", () -> {
            if(EnvUtil.isAndroid()) {
                driver.waitUntilElementVisible(PIN_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Confirm your code"), WAIT_10_S);

                driver.waitUntilElementVisible(PIN_BACK_BUTTON.byAndroidXpathWithAttribute("content-desc", "Navigate up"), WAIT_10_S);

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

                //back button
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeNavigationBar[@name=\"Change code\"]/XCUIElementTypeButton[1]"), WAIT_10_S);

                driver.safeClick(PIN_INFO_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.TITLE), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_1), WAIT_10_S);
                driver.waitUntilElementVisible(PIN_INFO_TEXT.byIosXpathWithName(PinSetupData.BULLET_POINT_2), WAIT_10_S);
                driver.safeClick(PIN_INFO_CLOSE_BUTTON, WAIT_10_S);

                driver.waitUntilElementVisible(PIN_TITLE.byIosXpathWithNameAndAttribute("value", "Please confirm the new PIN"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.safeSendKeys(SET_PIN_INPUT, WAIT_10_S, APP_PIN_CHANGE);
        });

        Allure.step("Land on Security page", () -> {
            if(EnvUtil.isAndroid()) {
                driver.waitUntilElementVisible(SECURITY_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Security"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                //TODO: iOS does not land on Security page
                //driver.waitUntilElementVisible(SECURITY_TITLE, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
