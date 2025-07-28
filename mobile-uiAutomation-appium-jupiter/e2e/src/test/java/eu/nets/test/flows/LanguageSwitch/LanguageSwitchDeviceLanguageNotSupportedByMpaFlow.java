package eu.nets.test.flows.LanguageSwitch;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.flows.data.models.MpaUser;
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
import java.util.Arrays;
import java.util.List;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LanguageSwitchDeviceLanguageNotSupportedByMpaFlow extends AbstractFlow {
    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return AndroidSnapshot.NO_MPA;
    }

    @ParameterizedTest(name = "[{index}] {1}, {0}")
    @MethodSource("eu.nets.test.flows.data.LanguageSwitch.LanguageSwitchData#streamLanguagesNotSupportedByMpa")
    @Epic("Language Switch")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6597")
    @Story("LanguageSwitchDeviceLanguageNotSupportedByMpa")
    @Description("")
    protected void runTest(
            MpaUser user,
            MpaLanguage desiredAppLanguage
    ) throws IOException, InterruptedException {
        run(user, desiredAppLanguage);
    }

    protected void run(
            MpaUser user,
            MpaLanguage desiredAppLanguage
    ) throws IOException, InterruptedException {
        List<MpaLanguage> expectedInAppLanguages = Arrays.stream(MpaLanguage.values())
                .filter(lang -> lang.isSupportedByMpa() && !lang.equals(MpaLanguage.FALLBACK))
                .toList();

        new LanguageSwitchBaseFlow().run(user, desiredAppLanguage, MpaLanguage.FALLBACK, expectedInAppLanguages);
    }
}
