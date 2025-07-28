package eu.nets.test.flows.VerifyEmptyState;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.VerifyEmptyState.VerifyEmptyStateData;
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

import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TAB;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyEmptySettlementsListFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyEmptyState.VerifyEmptyStateData#stream")
    @Epic("")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6778")
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

        Allure.step("Open OVERVIEW -> Accounting -> Settlements and verify empty state", () -> {
            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Settlement"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "No Payouts yet"), WAIT_10_S);
                driver.scrollPercent(
                        driver.waitUntilElementVisible(By.xpath("//android.widget.ScrollView"), WAIT_10_S),
                        "down",
                        50
                );
                driver.waitUntilElementVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT.byAndroidXpathWithResourceIdAndAttribute("text", VerifyEmptyStateData.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeApplication[@name=\"MyPayments Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton[1]"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE, WAIT_10_S);
                driver.swipePercent(
                        driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeTable[@name=\"settlement_table_view\"]"), WAIT_10_S),
                        "up",
                        50
                );
                driver.waitUntilElementVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT.byIosXpathWithName(VerifyEmptyStateData.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TEXT), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
