package eu.nets.test.flows.BAUStatements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
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
import org.openqa.selenium.By;

import java.io.IOException;

import static eu.nets.test.core.enums.AndroidSnapshot.NO_MPA;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_APPLY_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DONE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_FILTER;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENTS_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RECORD_INVOICE_NO;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SHARE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENT_NO_INPUT;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TAB;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TITLE;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BAUStatementsShareDetailsFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

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

            if(EnvUtil.isAndroid()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", "Accounting"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Statement"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byAndroidXpathWithAttribute("text", "Invoices"), WAIT_10_S);

            } else if (EnvUtil.isIos()) {
                driver.safeClick(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TITLE, WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byIosXpathWithName("Statement"), WAIT_10_S);
                driver.waitUntilElementVisible(ACCOUNTING_TAB.byIosXpathWithName("Invoices"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }

            driver.waitUntilElementVisible(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);
            driver.waitUntilElementVisible(ACCOUNTING_BACK_BUTTON, WAIT_10_S);
        });

        Allure.step("Apply Statement no filter", () -> {
            driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);

            if(EnvUtil.isAndroid()) {
                driver.safeClick(ACCOUNTING_FILTER.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S);
                driver.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S, BAUStatementsData.STATEMENT_NO_VALID);
                driver.safeClick(ACCOUNTING_DONE_BUTTON, WAIT_10_S);
                driver.safeClick(ACCOUNTING_APPLY_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", "APPLY 1 FILTER"), WAIT_10_S);
            } else if (EnvUtil.isIos()) {
                driver.safeClick(ACCOUNTING_FILTER.byIosXpathWithName("Statement no"), WAIT_10_S);
                driver.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT, WAIT_10_S, BAUStatementsData.STATEMENT_NO_VALID);
                driver.safeClick(By.xpath("(//XCUIElementTypeButton[@name=\"APPLY\"])[2]"), WAIT_10_S);
                driver.safeClick(ACCOUNTING_APPLY_BUTTON.byIosXpathWithName("APPLY 1 FILTER"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Verify results, open statement PDF and verify Share feature", () -> {
            if(EnvUtil.isAndroid()) {
                driver.safeClick(ACCOUNTING_RECORD_INVOICE_NO, WAIT_10_S);
                driver.safeClick(ACCOUNTING_SHARE_BUTTON, WAIT_10_S);
                assertTrue(
                        driver.waitUntilElementVisible(By.xpath("//android.widget.TextView[@resource-id=\"com.android.intentresolver:id/headline\"]"), WAIT_10_S).getText().contains("Sharing 1 file"),
                        "[Sharing message not displayed as expected] contains(\"Sharing 1 file\")"
                );
                assertTrue(
                        driver.waitUntilElementVisible(By.xpath("//android.widget.TextView[@resource-id=\"com.android.intentresolver:id/content_preview_filename\"]"), WAIT_10_S).getText().contains(".pdf"),
                        "[PDF file name not displayed as expected] contains(\".pdf\")"
                );
            } else if (EnvUtil.isIos()) {
                driver.safeClick(By.xpath("//XCUIElementTypeStaticText[@name=\"" + BAUStatementsData.DATE_MONTH_YYYY + "\"]"), WAIT_10_S);
                driver.safeClick(ACCOUNTING_RECORD_INVOICE_NO.byIosXpathWithName("Statement no: " + BAUStatementsData.STATEMENT_NO_VALID), WAIT_10_S);
                driver.safeClick(ACCOUNTING_SHARE_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(By.xpath("//XCUIElementTypeOther[@name=\"PDF Document · 19 KB\"]"), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });
    }
}
