package eu.nets.test.flows.VerifyElements.VerifyTapToPayElements;


import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.*;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.flows.data.shared.CountryData;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.*;
import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyTapToPayElementsFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;

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

    @ParameterizedTest(name = "[{index}] {0} {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifyTapToPayElements.VerifyTapToPayElementsData#stream")
    @Epic("Verify Tap to Pay buttons presence")
    @Feature("")
    @Story("")
    @Description("Verify that the right users can see the Tap to Pay button depending on platform and country")
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
                driver.getPageSource();
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

        //Wrapping the two main scenarios with an if-else

        if (EnvUtil.isAndroid()) {
            // On Android, Nexi SoftPOS button is visible only on DACH accounts with license associated
            Allure.step("[Android specific] Check presence of Nexi SoftPOS button depending on user type", () -> {
                boolean isAndroidTTPOACountry = CountryData.androidNexiSoftPOSCountries.contains(user.country());
                if (isAndroidTTPOACountry && user.description().contains(("android_licence"))) {
                    // In this case the user has Android licenses
                    driver.waitUntilElementVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                } else if (isAndroidTTPOACountry && user.description().contains(("ios_licence"))) {
                    // In this case, the user has iOS licences, so the button should not be visible
                    driver.waitUntilElementNotVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                } else {
                    // In this case, the user does not belong to a country where TTPOA has been released or the user does not have licenses at all
                    driver.waitUntilElementNotVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                }
            });
        } else if (EnvUtil.isIos()) {
            // On iOS, Nexi SoftPOS and Nets SoftPOS button depend on country and licence presence
            Allure.step("[iOS specific] Check presence of Nexi/Nets SoftPOS button depending on user type", () -> {
                boolean isIOSNetsSoftPOSCountry = CountryData.iOSNetsSoftPOSCountries.contains(user.country());
                boolean isIOSNexiSoftPOSCountry = CountryData.iOSNexiSoftPOSCountries.contains(user.country());

                // Checks for Nordics account
                if (isIOSNetsSoftPOSCountry) {
                    // Here the Nets SoftPOS button must be visible since the account belongs to a country where Nets SoftPOS has been released
                    driver.waitUntilElementVisible(NETS_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                } else {
                    // In this case, the user does not belong to a country where Nets SoftPOS has been released
                    driver.waitUntilElementNotVisible(NETS_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                }

                // Checks for DACH account
                if (isIOSNexiSoftPOSCountry && user.description().contains("ios_licence")) {
                    // In this case, the user is DACH and has iOS licences, so Nexi SoftPOS is visible
                    driver.waitUntilElementVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                } else if (isIOSNexiSoftPOSCountry && user.description().contains("android_licence")) {
                    // In this case, the user is DACH but has Android licenses, so Nexi SoftPOS is not visible
                    driver.waitUntilElementNotVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                } else {
                    // In this case, the user is not from a DACH country or does not have iOS licenses, so Nexi SoftPOS is not visible
                    driver.waitUntilElementNotVisible(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
                }
            });
        } else {
            throw new UnsupportedPlatformException();
        }
    }
}
