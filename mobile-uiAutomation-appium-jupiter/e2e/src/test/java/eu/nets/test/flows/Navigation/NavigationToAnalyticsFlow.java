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
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static eu.nets.test.core.enums.AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP;
import static eu.nets.test.core.enums.MpaWidget.ACTIVITY_GRAPH;
import static eu.nets.test.core.enums.MpaWidget.ANALYTICS_CLOSE_ICON;
import static eu.nets.test.core.enums.MpaWidget.ANALYTICS_SCREEN_TITLE;
import static eu.nets.test.core.enums.MpaWidget.ANALYTICS_SECTION_TAB;
import static eu.nets.test.core.enums.MpaWidget.AVERAGE_PURCHASE_GRAPH;
import static eu.nets.test.core.enums.MpaWidget.INTERNATIONAL_CUSTOMERS_GRAPH;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ANALYTICS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.RECURRING_CUSTOMERS_GRAPH;
import static eu.nets.test.core.enums.MpaWidget.TURNOVER_GRAPH;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NavigationToAnalyticsFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

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
    @Epic("Navigation to Analytics")
    @Feature("")
    @Story("Navigation to Analytics and check that the elements displayed are correct")
    @Description("Navigation to Analytics and check that the elements displayed are correct")
    protected void runTest(
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        run(appLanguage, false);
    }

    protected void run(
            MpaLanguage appLanguage,
            boolean testInAppPin
    ) throws IOException, InterruptedException {
        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: load startup snapshot " + startupAndroidSnapshot(), () -> {
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

        // Navigation into Analytics section
        Allure.step("Navigation into Analytics section" + appLanguage, () -> {
            if (EnvUtil.isAndroid()) {
                WebElement analytics_icon = driver.waitUntilElementVisible(OVERVIEW_ANALYTICS_BUTTON.byAndroidXpathWithResourceIdAndIndex(3, true), WAIT_10_S);
                analytics_icon.click();
            } else if (EnvUtil.isIos()) {
                WebElement analytics_icon = driver.waitUntilElementVisible(OVERVIEW_ANALYTICS_BUTTON, WAIT_10_S);
                analytics_icon.click();
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Check that all the correct elements are visible for the Analytics tab", () -> {
            // Close the tutorial graph
            driver.safeClick(ANALYTICS_CLOSE_ICON, WAIT_10_S);

            driver.waitUntilElementVisible(ANALYTICS_SCREEN_TITLE, WAIT_10_S);
            driver.waitUntilElementVisible(TURNOVER_GRAPH, WAIT_10_S);
            driver.waitUntilElementVisible(ACTIVITY_GRAPH, WAIT_10_S);
            driver.waitUntilElementVisible(INTERNATIONAL_CUSTOMERS_GRAPH, WAIT_10_S);
            driver.waitUntilElementVisible(AVERAGE_PURCHASE_GRAPH, WAIT_10_S);
            driver.waitUntilElementNotVisible(ANALYTICS_SECTION_TAB, WAIT_10_S);
            driver.waitUntilElementNotVisible(RECURRING_CUSTOMERS_GRAPH, WAIT_10_S);
        });
    }
}
