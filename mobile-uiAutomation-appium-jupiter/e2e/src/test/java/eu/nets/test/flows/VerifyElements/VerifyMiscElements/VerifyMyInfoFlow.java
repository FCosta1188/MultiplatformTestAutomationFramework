package eu.nets.test.flows.VerifyElements.VerifyMiscElements;

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
import org.openqa.selenium.By;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.MYINFO_ADDRESS;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_ADDRESS_LABEL;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_COMPANIES_INFORMATION;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_COMPANY_INFORMATION;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_EMAIL;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_FULLNAME;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_ORG;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_PERSONAL_INFORMATION;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_PHONE;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_VATNO;
import static eu.nets.test.core.enums.MpaWidget.MYINFO_VATNO_LABEL;
import static eu.nets.test.core.enums.MpaWidget.PROFILE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SIDEMENU_MYINFO;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyMyInfoFlow extends AbstractFlow {
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

    @Override
    public String flowClassName() {
        return "VerifyMyInfoFlow";
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifyMiscElements.VerifyMyInfoData#stream")
    @Epic("")
    @Feature(
            """
                    https://nexigroup-germany.atlassian.net/browse/MSA-6350
                    https://nexigroup-germany.atlassian.net/browse/MSA-6351
                    """
    )
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
        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: load loggedInAndroidSnapshot: " + user.loggedInAndroidSnapshot(), () -> {
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

        Allure.step("Open My Info and verify page layout", () -> {
            driver.safeClick(PROFILE_BUTTON, WAIT_10_S);
            driver.safeClick(SIDEMENU_MYINFO, WAIT_10_S);
            EnvUtil.safeSleep(10000);

            if (user.description().contains("one company")) {
                Allure.step("[Test Case]: one company", () -> {
                    driver.waitUntilElementVisible(MYINFO_VATNO_LABEL, WAIT_10_S);
                    driver.waitUntilElementVisible(MYINFO_ADDRESS_LABEL, WAIT_10_S);

                    if (EnvUtil.isAndroid()) {
                        driver.waitUntilElementVisible(MYINFO_COMPANY_INFORMATION.byAndroidXpathWithResourceIdAndAttribute("text", "Company Information"),
                                WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_ORG.byAndroidXpathWithResourceIdAndAttribute("text", user.org()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_VATNO.byAndroidXpathWithResourceIdAndAttribute("text", user.vat()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_ADDRESS.byAndroidXpathWithResourceIdAndAttribute("text", user.address()), WAIT_10_S);
                    } else if (EnvUtil.isIos()) {
                        driver.waitUntilElementVisible(MYINFO_COMPANY_INFORMATION, WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_ORG.byIosXpathWithName(user.org()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_VATNO.byIosXpathWithName(user.vat()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_ADDRESS.byIosXpathWithName(user.address()), WAIT_10_S);
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                });
            } else if (user.description().contains("multiple companies")) {
                Allure.step("[Test Case]: multiple companies", () -> {
                    String[] orgs = user.org().split("\\|");

                    if (EnvUtil.isAndroid()) {
                        driver.waitUntilElementVisible(MYINFO_COMPANIES_INFORMATION.byAndroidXpathWithResourceIdAndAttribute("text", "Companies information"),
                                WAIT_30_S);
                        for (String org : orgs) {
                            driver.waitUntilElementVisible(MYINFO_ORG.byAndroidXpathWithResourceIdAndAttribute("text", org), WAIT_10_S);
                            driver.scrollPercent(driver.findElement(By.xpath(
                                    "//androidx.recyclerview.widget.RecyclerView[@resource-id=\"eu.nets.mypayments.mock:id/companyInfoList\"]")), "down", 25);
                        }
                    } else if (EnvUtil.isIos()) {
                        driver.waitUntilElementVisible(MYINFO_COMPANIES_INFORMATION, WAIT_30_S);
                        for (String org : orgs) {
                            driver.waitUntilElementVisible(MYINFO_ORG.byIosXpathWithName(org), WAIT_10_S);
                            driver.swipePercent(driver.findElement(By.xpath(
                                            "//XCUIElementTypeApplication[@name=\"MyPayments " +
                                                    "Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther" +
                                                    "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther" +
                                                    "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")),
                                    "up",
                                    25);
                        }
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                });
            } else if (user.description().contains("no phone number")) {
                Allure.step("[Test Case]: no phone number", () -> {
                    if (EnvUtil.isAndroid()) {
                        driver.waitUntilElementVisible(MYINFO_PERSONAL_INFORMATION.byAndroidXpathWithResourceIdAndAttribute("text", "Personal information"),
                                WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_FULLNAME.byAndroidXpathWithResourceIdAndAttribute("text", user.fullName()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_EMAIL.byAndroidXpathWithResourceIdAndAttribute("text", user.email()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_PHONE.byAndroidXpathWithResourceIdAndAttribute("text", "Missing phone number"), WAIT_10_S);
                    } else if (EnvUtil.isIos()) {
                        driver.waitUntilElementVisible(MYINFO_PERSONAL_INFORMATION, WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_FULLNAME.byIosXpathWithName(user.fullName()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_EMAIL.byIosXpathWithName(user.email()), WAIT_10_S);
                        driver.waitUntilElementVisible(MYINFO_PHONE.byIosXpathWithName("Missing phone number"), WAIT_10_S);
                    } else {
                        throw new UnsupportedPlatformException();
                    }
                });
            } else {
                throw new RuntimeException("User description does not match any required test cases: " + user.description());
            }
        });
    }
}
