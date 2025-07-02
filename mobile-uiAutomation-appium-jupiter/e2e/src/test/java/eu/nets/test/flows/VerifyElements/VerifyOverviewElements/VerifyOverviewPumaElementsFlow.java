package eu.nets.test.flows.VerifyElements.VerifyOverviewElements;

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

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ACCOUNTING_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_ANALYTICS_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_TRANSACTION_BUTTON;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyOverviewPumaElementsFlow extends AbstractFlow {
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
        return "VerifyOverviewPumaElementsFlow";
    }

    @ParameterizedTest(name = "[{index}] {0} {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifyOverviewElements.VerifyPumaElementsData#streamPumaAccount")
    @Epic("Verify PUMA Overview Elements")
    @Feature("")
    @Story("")
    @Description("Checks that the Overview elements displayed on different types of PUMA users are correct")
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

        Allure.step("Check presence of correct elements based on agreement type", () -> {
            if (user.description().contains("puma_both") || user.description().contains("acquiring_only")) {
                // Check that in the Overview page, the Transactions, Accounting and Analytics page are displayed
                driver.waitUntilElementVisible(OVERVIEW_TRANSACTION_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(OVERVIEW_ANALYTICS_BUTTON, WAIT_10_S);
            } else if (user.description().contains("terminal_only")) {
                driver.waitUntilElementVisible(OVERVIEW_TRANSACTION_BUTTON, WAIT_10_S);
                driver.waitUntilElementVisible(OVERVIEW_ACCOUNTING_BUTTON, WAIT_10_S);
                driver.waitUntilElementNotVisible(OVERVIEW_ANALYTICS_BUTTON, WAIT_10_S);
            } else {
                throw new RuntimeException("Unsupported agreement type or agreement type not specified correctly");
            }
        });
    }
}
