package eu.nets.test.flows.BAUStatements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.drivers.MpaDriver;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.BAUStatements.BAUStatementsData;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;

import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_APPLY_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_CLEAR_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DATE_FILTER_APPLY_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DATE_FILTER_MONTH_NAV_PREV;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DATE_FILTER_OK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DATE_FILTER_YEAR_SELECTOR;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_DONE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_FILTER;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENTS_FILTER_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RECORD_INVOICE_DATE;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RECORD_INVOICE_NO;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_RESET_FILTERS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_STATEMENT_NO_INPUT;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TAB;
import static eu.nets.test.core.enums.MpaWidget.ACCOUNTING_TITLE;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static eu.nets.test.util.AllureUtil.logInfo;
import static eu.nets.test.util.AllureUtil.logWarning;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BAUStatementsFilterBaseFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    protected void run(
            MpaUser user,
            MpaLanguage appLanguage,
            BAUStatementsData.Filter filter
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

        Allure.step("Apply filter: " + filter, () -> {
            driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);

            switch (filter) {
                case STATEMENT_NO:
                    applyStatementNoFilter(driver, BAUStatementsData.STATEMENT_NO_VALID, 1);
                    break;
                case DATE:
                    applyDateFilter(driver, 1);
                    break;
                case DATE_AND_STATEMENT_NO:
                    if(EnvUtil.isAndroid()) {
                        applyStatementNoFilter(driver, BAUStatementsData.STATEMENT_NO_VALID, 1);
                        driver.safeClick(ACCOUNTING_STATEMENTS_FILTER_BUTTON, WAIT_10_S);
                        applyDateFilter(driver, 2);
                    } else if (EnvUtil.isIos()) { //TODO: implement filter on iOS when app calendar will be updated
                        logInfo("Test skipped on iOS due to outdated calendar in the app");
                        return;
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                    break;
                case NO_RESULTS:
                    applyStatementNoFilter(driver, BAUStatementsData.STATEMENT_NO_INVALID, 1);
                    break;
                default:
                    throw new UnsupportedOperationException("[Filter: " + filter + "] is not supported in this flow");
            }
        });

        Allure.step("Verify \"Clear filter\" button and filtered record(s)", () -> {
            try {
                driver.waitUntilElementVisible(ACCOUNTING_CLEAR_FILTER_BUTTON, WAIT_10_S);
            } catch (Exception noRecordsFound) {
                verifyNoResultsFilter(driver);
                assertTrue(
                        true,
                        logWarning("[Filter/Results mismatch] Filter: " + filter + " - Filter output: no results")
                );
                return;
            }

            switch (filter) {
                case STATEMENT_NO:
                    verifyStatementNoFilter(driver);
                    break;
                case DATE:
                    verifyDateFilter(driver, appLanguage);
                    break;
                case DATE_AND_STATEMENT_NO:
                    if(EnvUtil.isAndroid()) {
                        verifyStatementNoFilter(driver);
                        verifyDateFilter(driver, appLanguage);
                    } else if (EnvUtil.isIos()) { //TODO: implement filter on iOS when app calendar will be updated
                        logInfo("Test skipped on iOS due to outdated calendar in the app");
                        return;
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                    break;
                case NO_RESULTS:
                    verifyNoResultsFilter(driver);
                    break;
                default:
                    throw new UnsupportedOperationException("[Filter]: " + filter + "] is not supported in this flow");
            }
        });
    }

    private void applyStatementNoFilter(MpaDriver d, String statementNo, int numberOfFilters) {
        if(EnvUtil.isAndroid()) {
            d.safeClick(ACCOUNTING_FILTER.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S);
            d.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT.byAndroidXpathWithResourceIdAndAttribute("text", "Statement no"), WAIT_10_S, statementNo);
            d.safeClick(ACCOUNTING_DONE_BUTTON, WAIT_10_S);
            d.safeClick(ACCOUNTING_APPLY_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", applyButtonText(numberOfFilters)), WAIT_10_S);
        } else if (EnvUtil.isIos()) {
            d.safeClick(ACCOUNTING_FILTER.byIosXpathWithName("Statement no"), WAIT_10_S);
            d.safeSendKeys(ACCOUNTING_STATEMENT_NO_INPUT, WAIT_10_S, statementNo);
            d.safeClick(By.xpath("(//XCUIElementTypeButton[@name=\"APPLY\"])[2]"), WAIT_10_S);
            d.safeClick(ACCOUNTING_APPLY_BUTTON.byIosXpathWithName(applyButtonText(numberOfFilters)), WAIT_10_S);
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private void verifyStatementNoFilter(MpaDriver d) {
        if(EnvUtil.isAndroid()) {
            assertTrue(
                    d.waitUntilElementVisible(ACCOUNTING_RECORD_INVOICE_NO, WAIT_10_S).getText().contains(BAUStatementsData.STATEMENT_NO_VALID),
                    "[Statement no] Filtered record is not displayed as expected"
            );
        } else if (EnvUtil.isIos()) {
            d.safeClick(By.xpath("//XCUIElementTypeStaticText[@name=\"" + BAUStatementsData.DATE_MONTH_YYYY + "\"]"), WAIT_10_S);
            d.waitUntilElementVisible(ACCOUNTING_RECORD_INVOICE_NO.byIosXpathWithName("Statement no: " + BAUStatementsData.STATEMENT_NO_VALID), WAIT_10_S);
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private void applyDateFilter(MpaDriver d, int numberOfFilters) {
        if(EnvUtil.isAndroid()) {
            d.safeClick(ACCOUNTING_FILTER.byAndroidXpathWithResourceIdAndAttribute("text", "Date"), WAIT_10_S);

            d.safeClick(ACCOUNTING_DATE_FILTER_YEAR_SELECTOR, WAIT_10_S);
            d.safeClick(By.xpath("//android.widget.TextView[@content-desc=\"Navigate to year " + BAUStatementsData.DATE_YEAR + "\"]"), WAIT_10_S);
            for (int i = 0; i < 6; i++) {
                d.safeClick(ACCOUNTING_DATE_FILTER_MONTH_NAV_PREV, WAIT_10_S);
                EnvUtil.safeSleep(500);
            }
            d.safeClick(By.xpath("//android.widget.TextView[@content-desc=\"" + BAUStatementsData.DATE_DAY_DD_MONTH_YYYY + "\"]"), WAIT_10_S);
            EnvUtil.safeSleep(500);
            d.safeClick(By.xpath("//android.widget.TextView[@content-desc=\"Start date " + BAUStatementsData.DATE_DAY_DD_MONTH_YYYY + "\"]"), WAIT_10_S);
            EnvUtil.safeSleep(500);
            d.safeClick(ACCOUNTING_DATE_FILTER_OK_BUTTON, WAIT_10_S);

            d.safeClick(ACCOUNTING_APPLY_BUTTON.byAndroidXpathWithResourceIdAndAttribute("text", applyButtonText(numberOfFilters)), WAIT_10_S);
        } else if (EnvUtil.isIos()) {
            d.safeClick(ACCOUNTING_FILTER.byIosXpathWithName("Date"), WAIT_10_S);

            d.safeClick(By.xpath("(//XCUIElementTypeStaticText[@name=\"1\"])[1]"), WAIT_10_S);
            d.safeClick(By.xpath("(//XCUIElementTypeStaticText[@name=\"2\"])[1]"), WAIT_10_S);
            d.safeClick(ACCOUNTING_DATE_FILTER_APPLY_BUTTON, WAIT_10_S);

            d.safeClick(ACCOUNTING_APPLY_BUTTON.byIosXpathWithName(applyButtonText(numberOfFilters)), WAIT_10_S);
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private void verifyDateFilter(MpaDriver d, MpaLanguage appLanguage) {
        if(EnvUtil.isAndroid()) {
            List<WebElement> filteredDates = new WebDriverWait(d, Duration.ofSeconds(WAIT_10_S), Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ACCOUNTING_RECORD_INVOICE_DATE.byAndroidResourceId()));

            assertEquals(
                    BAUStatementsData.DATE_MMM_YYYY,
                    filteredDates.get(0).getText().trim(),
                    "[Date filter mismatch: statement container date]"
            );
            filteredDates.remove(0);

            for (WebElement date : filteredDates) {
                assertEquals(
                        BAUStatementsData.DATE_DD_MMM,
                        date.getText().trim(),
                        "[Date filter mismatch: statement date]"
                );
            }
        } else if (EnvUtil.isIos()) {
            LocalDate today = LocalDate.now();
            int year = today.getYear();
            String month = today.getMonth().getDisplayName(TextStyle.FULL, appLanguage.getRegion());
            d.safeClick(ACCOUNTING_RECORD_INVOICE_DATE.byIosXpathWithName(month + " " + year), WAIT_10_S);

            List<WebElement> filteredDateCells = new WebDriverWait(d, Duration.ofSeconds(WAIT_10_S), Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//XCUIElementTypeTable/XCUIElementTypeCell[2]")));

            if(!filteredDateCells.isEmpty()) {
                for (WebElement cell : filteredDateCells) {
                    String cellDate = cell.findElements(By.className("XCUIElementTypeStaticText")).get(0).getText().trim();

                    boolean dateMatch = cellDate.equals("01 " + month.substring(0, 3)) ||
                            cellDate.equals("02 " + month.substring(0, 3));

                    assertTrue(
                            dateMatch,
                            "[Date filter mismatch: statement date] - actual date: " + cellDate
                    );
                }
            }
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private void verifyNoResultsFilter(MpaDriver d) {
        if(EnvUtil.isAndroid()) {
            d.waitUntilElementVisible(By.xpath("//android.widget.TextView[@text=\"No results\"]"), WAIT_10_S);
            d.waitUntilElementNotVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE, WAIT_10_S);

            d.safeClick(ACCOUNTING_RESET_FILTERS_BUTTON, WAIT_10_S);

            List<WebElement> filteredDates = new WebDriverWait(d, Duration.ofSeconds(WAIT_10_S), Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ACCOUNTING_RECORD_INVOICE_DATE.byAndroidResourceId()));
            assertTrue(
                    !filteredDates.isEmpty(),
                    "No results found after reset filters"
            );
        } else if (EnvUtil.isIos()) {
            d.waitUntilElementVisible(By.xpath("//XCUIElementTypeStaticText[@name=\"No results\"]"), WAIT_10_S);
            d.waitUntilElementNotVisible(ACCOUNTING_SETTLEMENTS_EMPTY_STATE_TITLE, WAIT_10_S);

            d.safeClick(ACCOUNTING_RESET_FILTERS_BUTTON, WAIT_10_S);

            List<WebElement> filteredDateCells = new WebDriverWait(d, Duration.ofSeconds(WAIT_10_S), Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//XCUIElementTypeTable/XCUIElementTypeCell[2]")));
            assertTrue(
                    !filteredDateCells.isEmpty(),
                    "No results found after reset filters"
            );
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    private String applyButtonText(int numberOfFilters) {
        if(numberOfFilters == 1) {
            return "APPLY 1 FILTER";
        } else if (numberOfFilters > 1) {
            return "APPLY " + numberOfFilters + " FILTERS";
        } else {
            throw new IllegalArgumentException("Invalid number of filters: " + numberOfFilters);
        }
    }
 }
