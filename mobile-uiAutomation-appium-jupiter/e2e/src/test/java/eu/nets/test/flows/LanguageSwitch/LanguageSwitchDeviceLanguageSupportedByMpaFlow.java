package eu.nets.test.flows.LanguageSwitch;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.models.MpaUser;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;

public class LanguageSwitchDeviceLanguageSupportedByMpaFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;
    private final int WAIT_40_S = 40;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @Override
    public String flowClassName() {
        return "LanguageSwitchDeviceLanguageSupportedByMpaFlow";
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.LanguageSwitch.LanguageSwitchData#streamDeviceLanguageSupportedByMpa")
    @Epic("Language Switch")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6597")
    @Story("LanguageSwitchDeviceLanguageSupportedByMpa")
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

    }
}
