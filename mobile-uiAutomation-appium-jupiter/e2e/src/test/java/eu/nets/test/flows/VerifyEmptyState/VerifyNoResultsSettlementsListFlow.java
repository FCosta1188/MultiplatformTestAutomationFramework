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

import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_APPLY_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_CLEAR_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DONE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_FILTER;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SETTLEMENTS_INVOICES_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENTS_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RESET_FILTERS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENT_NO_INPUT;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TAB;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TITLE;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyNoResultsSettlementsListFlow extends AbstractFlow {
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

        Allure.step("Open OVERVIEW -> Accounting -> Settlements and verify No Results filter", () -> {
            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Settlement"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Invoices"), WAIT_10_S);

                driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);
                driver.safeClick(ACCOUNTING_FILTER.byAndroidXpathWithResourceIdAndAttribute("text", "Amount"), WAIT_10_S);
                driver.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT.byAndroidXpathWithResourceIdAndAttribute("text", "Exact amount"), WAIT_10_S, VerifyEmptyStateData.AMOUNT_INVALID);
                driver.safeClick(ACCOUNTING_DONE_BUTTON, WAIT_10_S);
                driver.safeClick(ACCOUNTING_APPLY_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "APPLY 1 FILTER"), WAIT_10_S);

                driver.waitUntilElementVisible(ACCOUNTING_CLEAR_FILTER_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_RESET_FILTERS_BUTTON, WAIT_20_S);
                driver.waitUntilElementVisible(By.xpath("//android.widget.TextView[@text=\"No results\"]"), WAIT_10_S);
                driver.waitUntilElementNotVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE, WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TITLE, WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeApplication[@name=\"MyPayments Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton[1]"), WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeApplication[@name=\"MyPayments Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton[2]"), WAIT_10_S);

                driver.safeClick(ACCOUNTING_SETTLEMENTS_INVOICES_FILTER_BUTTON, WAIT_10_S);
                driver.safeClick(ACCOUNTING_FILTER.byIosXpathWithName("Amount"), WAIT_10_S);
                driver.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT, WAIT_10_S, VerifyEmptyStateData.AMOUNT_INVALID);
                driver.safeClick(By.xpath("(//XCUIElementTypeButton[@name=\"APPLY\"])[2]"), WAIT_10_S);
                driver.safeClick(ACCOUNTING_APPLY_BUTTON.byIosXpathWithName("APPLY 1 FILTER"), WAIT_10_S);

                driver.waitUntilElementVisible(ACCOUNTING_CLEAR_FILTER_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_RESET_FILTERS_BUTTON, WAIT_20_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeStaticText[@name=\"No results\"]"), WAIT_10_S);
                driver.waitUntilElementNotVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE, WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
