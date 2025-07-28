package eu.nets.test.flows.BAUStatements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.BAUStatements.BAUStatementsData;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;

import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_APPLY_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_CLEAR_ALL_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DONE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_FILTER;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_FILTERS_CLOSE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENTS_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RECORD_INVOICE_DATE;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENT_NO_INPUT;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TAB;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TITLE;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BAUStatementsFilterClearAllFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.BAUStatements.BAUStatementsData#stream")
    @Epic("")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6720")
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
        if(!EnvUtil.isAndroid()) {
            assertTrue(true, "Flow implemented on Android only.");
            return;
        }

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

        Allure.step("Open OVERVIEW -> Accounting -> Statement tab (selected by default) and verify elements", () -> {
            driver.safeClick(OVERVIEW_DASHBOARD_BUTTON, WAIT_10_S);

            driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
            driver.waitUntilElementVisible(ACCOUNTING_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
            driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Statement"), WAIT_10_S);
            driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Invoices"), WAIT_10_S);

            driver.waitUntilElementVisible(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);
            driver.waitUntilElementVisible(ACCOUNTING_BACK_BUTTON, WAIT_10_S);
        });

        Allure.step("Apply Statement no filter", () -> {
            driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);

            driver.safeClick(ACCOUNTING_FILTER.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S);
            driver.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S, BAUStatementsData.STATEMENT_NO_VALID);
            driver.safeClick(ACCOUNTING_DONE_BUTTON, WAIT_10_S);
            driver.safeClick(ACCOUNTING_APPLY_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "APPLY 1 FILTER"), WAIT_10_S);
        });

        Allure.step("Clear all filters and verify Statements list", () -> {
            driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);

            driver.safeClick(ACCOUNTING_CLEAR_ALL_BUTTON, WAIT_10_S);
            assertFalse(
                    Boolean.parseBoolean(driver.waitUntilElementVisible(ACCOUNTING_APPLY_BUTTON, WAIT_10_S).getAttribute("enabled")),
                    "[Element state] Apply button not disabled as expected after clearing all filters."
            );

            driver.safeClick(ACCOUNTING_FILTERS_CLOSE_BUTTON, WAIT_10_S);

            LocalDate today = LocalDate.now();
            int year = today.getYear();
            String month = today.getMonth().getDisplayName(TextStyle.FULL, appLanguage.getRegion());
            driver.waitUntilElementVisible(ACCOUNTING_RECORD_INVOICE_DATE.byAndroidXpathWithResourceIdAndAttribute("text", month + " " + year), WAIT_10_S);
        });
    }
}
