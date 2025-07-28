package eu.nets.test.flows.VerifyElements.VerifyMiscElements;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.VerifyElements.VerifyMiscElements.VerifyDeviceNotSupportedData;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

import static eu.nets.test.core.enums.MpaWidget.DEVICE_NOT_SUPPORTED_DESCRIPTION;
import static eu.nets.test.core.enums.MpaWidget.DEVICE_NOT_SUPPORTED_HEADER;
import static eu.nets.test.core.enums.MpaWidget.DEVICE_NOT_SUPPORTED_TITLE;
import static eu.nets.test.core.enums.MpaWidget.NETS_SOFTPOS_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.NEXI_SOFTPOS_DASHBOARD_BUTTON;
import static eu.nets.test.util.AllureUtil.logError;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyDeviceNotSupportedFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;

    private boolean androidSnapshotExists = false;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.VerifyElements.VerifyMiscElements.VerifyDeviceNotSupportedData#stream")
    @Epic("")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6661")
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
                androidSnapshotExists = true;
            });
        }

        Allure.step("Launch driver and set system language: " + appLanguage, () -> {
            if (appLanguage != null) {
                launchDriver(false, appLanguage.getLanguage(), appLanguage.getCountry());
            } else {
                throw new RuntimeException("Unable to launch driver due to: appLanguage is null");
            }
        });

        if ((EnvUtil.isAndroid() && !androidSnapshotExists) || EnvUtil.isIos()) {
            Allure.step("run RegistrationFlow - user: " + user, () -> {
                RegistrationFlow rFlow = new RegistrationFlow();
                rFlow.setDriver(this.driver);
                rFlow.run(user, appLanguage, false, false);
                this.setDriver(rFlow.getDriver());
            });
        }

        Allure.step("Open Nexi/Nets softpos page", () -> {
            if(EnvUtil.isAndroid() || (EnvUtil.isIos() && user.description().contains("german"))) {
                driver.safeClick(NEXI_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
            } else if (EnvUtil.isIos() && user.description().contains("nordics")) {
                driver.safeClick(NETS_SOFTPOS_DASHBOARD_BUTTON, WAIT_10_S);
            }
        });

        Allure.step("Verify Nexi/Nets softpos \"Device Not Supported\" page elements", () -> {
            if (user.description().contains("german")) {
                if(EnvUtil.isAndroid()) {
                    assertEquals(
                            VerifyDeviceNotSupportedData.HEADER,
                            driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_HEADER, WAIT_10_S).getText(),
                            "[DEVICE_NOT_SUPPORTED_HEADER mismatch]"
                    );
                    assertEquals(
                            VerifyDeviceNotSupportedData.TITLE,
                            driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_TITLE, WAIT_10_S).getText(),
                            "[DEVICE_NOT_SUPPORTED_TITLE mismatch]"
                    );
                    assertEquals(
                            VerifyDeviceNotSupportedData.DESCRIPTION_ANDROID_NEXI,
                            driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_DESCRIPTION, WAIT_10_S).getText(),
                            "[DEVICE_NOT_SUPPORTED_DESCRIPTION mismatch]"
                    );
                } else if (EnvUtil.isIos()) {
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_HEADER.byIosXpathWithName(VerifyDeviceNotSupportedData.HEADER), WAIT_10_S);
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_TITLE.byIosXpathWithName(VerifyDeviceNotSupportedData.TITLE), WAIT_10_S);
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_DESCRIPTION.byIosXpathWithName(VerifyDeviceNotSupportedData.DESCRIPTION_IOS_NEXI), WAIT_10_S);
                } else {
                    throw new UnsupportedPlatformException();
                }
            } else if (user.description().contains("nordics")) {
                if(EnvUtil.isAndroid()) {
                    throw new RuntimeException(logError("User not applicable on Android: " + user.description()));
                } else if (EnvUtil.isIos()) {
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_HEADER.byIosXpathWithName(VerifyDeviceNotSupportedData.HEADER), WAIT_10_S);
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_TITLE.byIosXpathWithName(VerifyDeviceNotSupportedData.TITLE), WAIT_10_S);
                    driver.waitUntilElementVisible(DEVICE_NOT_SUPPORTED_DESCRIPTION.byIosXpathWithName(VerifyDeviceNotSupportedData.DESCRIPTION_IOS_NETS), WAIT_10_S);
                } else {
                    throw new UnsupportedPlatformException();
                }
            } else {
                throw new RuntimeException(logError("User description does not match test requirements: " + user.description()));
            }
        });

        if (EnvUtil.isAndroid() && !androidSnapshotExists) {
            Allure.step("[Android specific]: save snapshot: " + user.loggedInAndroidSnapshot(), () -> {
                user.loggedInAndroidSnapshot().save();
            });
        }
    }
}
