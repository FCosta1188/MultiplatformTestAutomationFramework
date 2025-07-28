package eu.nets.test.flows.VerifyElements.VerifySMPElements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
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
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.AUTOMATIC_TIP_SUBTITLE;
import static eu.nets.test.core.enums.MpaWidget.AUTOMATIC_TIP_TITLE;
import static eu.nets.test.core.enums.MpaWidget.CHAT_WITH_US_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.EDIT_RECEIPT_SUBTITLE;
import static eu.nets.test.core.enums.MpaWidget.EDIT_RECEIPT_TITLE;
import static eu.nets.test.core.enums.MpaWidget.SUPPORT_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.TERMINALS_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.TERMINAL_CHAT_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.TERMINAL_LOCATION;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyTerminalSupportSMPSwissFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        if (EnvUtil.isAndroid()) {
            return null;
        } else if (EnvUtil.isIos()) {
            return null;
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    @ParameterizedTest(name = "[{index}] {0} {1} {2}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifySMPElements.VerifyTerminalSupportSMPElementsData#streamSwissAccount")
    @Epic("Verify SMP elements")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6479")
    @Story("Verify elements are correctly visible for Swiss SMP users")
    @Description("Check that every dynamic elements are not displayed for Swiss SMP users")
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

        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: load loggedInAndroidSnapshot:" + user.loggedInAndroidSnapshot(), () -> {
                if (user.loggedInAndroidSnapshot().exists()) {
                    user.loggedInAndroidSnapshot().load();
                } else {
                    throw new RuntimeException(logError("Required snapshot does not exist: " + user.loggedInAndroidSnapshot().getName() + ". Please create it" +
                            " before running tests."));
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
                rFlow.run(user, appLanguage, false, false);
                this.setDriver(rFlow.getDriver());
            });
        }

        // Navigation in Support section
        Allure.step("Go into Support section", () -> {
            WebElement supportDashboardButton = driver.waitUntilElementVisible(SUPPORT_DASHBOARD_BUTTON, WAIT_10_S);
            supportDashboardButton.click();
        });

        Allure.step("Check the Chat with us element is not visible", () -> {
            if (EnvUtil.isAndroid()) {
                driver.waitUntilElementNotVisible(CHAT_WITH_US_BUTTON.byAndroidXpathWithResourceIdAndAttribute("content-desc", "menu_icon_content_desc"),
                        WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementNotVisible(CHAT_WITH_US_BUTTON, WAIT_10_S);
            }
        });

        // Check in Terminals section
        Allure.step("Tap TERMINALS button and land on TERMINALS", () -> {
            WebElement overviewTerminalsButton = driver.waitUntilElementVisible(TERMINALS_DASHBOARD_BUTTON, WAIT_10_S);
            overviewTerminalsButton.click();
        });

        Allure.step("Check if the Edit receipt and Automatic tip elements are not visible", () -> {
            if (EnvUtil.isAndroid()) {
                driver.waitUntilElementNotVisible(EDIT_RECEIPT_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Edit receipt text"), WAIT_10_S);
                driver.waitUntilElementNotVisible(EDIT_RECEIPT_SUBTITLE.byAndroidXpathWithResourceIdAndAttribute("text", "The printed receipt"), WAIT_10_S);
                driver.waitUntilElementNotVisible(AUTOMATIC_TIP_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Automatic tip function"), WAIT_10_S);
                driver.waitUntilElementNotVisible(AUTOMATIC_TIP_SUBTITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Set it up"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.waitUntilElementNotVisible(EDIT_RECEIPT_TITLE, WAIT_10_S);
                driver.waitUntilElementNotVisible(EDIT_RECEIPT_SUBTITLE, WAIT_10_S);
                driver.waitUntilElementNotVisible(AUTOMATIC_TIP_TITLE, WAIT_10_S);
                driver.waitUntilElementNotVisible(AUTOMATIC_TIP_SUBTITLE, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        // Checks in Terminal details only if the account has physical terminals
        Allure.step("Open Terminal details to check if Chat with us element is not visible", () -> {
            if (EnvUtil.isAndroid()) {
                WebElement terminal_details = driver.waitUntilElementVisible(TERMINAL_LOCATION.byAndroidXpathWithResourceIdAndIndex(2, true), WAIT_10_S);
                terminal_details.click();
            } else if (EnvUtil.isIos()) {
                WebElement terminal_details = driver.waitUntilElementVisible(TERMINAL_LOCATION.byIosXpathWithNameAndIndex("Arrow_Right", 2), WAIT_10_S);
                terminal_details.click();
            } else {
                throw new UnsupportedPlatformException();
            }
            driver.waitUntilElementNotVisible(TERMINAL_CHAT_BUTTON, WAIT_10_S);
        });
    }
}
