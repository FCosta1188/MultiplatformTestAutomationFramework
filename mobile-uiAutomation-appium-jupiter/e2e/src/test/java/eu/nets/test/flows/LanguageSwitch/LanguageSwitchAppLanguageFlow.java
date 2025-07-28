package eu.nets.test.flows.LanguageSwitch;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
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
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static eu.nets.test.core.enums.MpaWidget.CHANGE_LANGUAGE_CONFIRM_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.PIN_INPUT;
import static eu.nets.test.core.enums.MpaWidget.PROFILE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SIDEMENU_LANGUAGE;
import static eu.nets.test.util.AllureUtil.logInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LanguageSwitchAppLanguageFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;

    private MpaLanguage newLanguage;
    private WebElement newLanguageCell;
    private String newLanguageNativeName;
    private String newLanguageLabel;
    private WebElement selectedLanguageCell;
    private String selectedLanguageNativeName;
    private String selectedLanguageLabel;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return AndroidSnapshot.NO_MPA;
    }

    @ParameterizedTest(name = "[{index}] {1}, {0}")
    @MethodSource("eu.nets.test.flows.data.LanguageSwitch.LanguageSwitchData#streamLanguagesSupportedByMpa")
    @Epic("Language Switch")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6597")
    @Story("LanguageSwitchAppLanguageSupportedByMpa")
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
                .filter(lang -> lang.isSupportedByMpa() && !lang.equals(desiredAppLanguage))
                .toList();

        LanguageSwitchBaseFlow lsbFlow = new LanguageSwitchBaseFlow();
        Allure.step("run LanguageSwitchBaseFlow - user: " + user, () -> {
            lsbFlow.setDriver(this.driver);
            lsbFlow.run(user, desiredAppLanguage, desiredAppLanguage, expectedInAppLanguages);
            this.setDriver(lsbFlow.getDriver());
        });


        Allure.step("Select another language and confirm change", () -> {
            // select the second language in the list as the new one
            newLanguageCell = lsbFlow.getLanguageCells(this.driver).get(1);
            newLanguageNativeName = lsbFlow.getCellLanguageNativeName(newLanguageCell);
            newLanguage = MpaLanguage.fromNativeName(newLanguageNativeName);
            newLanguageLabel = lsbFlow.getCellLanguageLabel(newLanguageCell);
            newLanguageCell.click();

            if(EnvUtil.isAndroid()) {
                driver.safeClick(CHANGE_LANGUAGE_CONFIRM_BUTTON, WAIT_10_S);
            } else if(EnvUtil.isIos()) {
                driver.safeClick(CHANGE_LANGUAGE_CONFIRM_BUTTON.byIosXpathWithName(desiredAppLanguage.capitalizeDictionaryEntry("confirm", false)), WAIT_10_S);
            } else {
                throw new UnsupportedPlatformException();
            }
        });

        Allure.step("Insert in-app pin after app has been reloaded with new language (if applicable)", () -> {
            try {
                if (EnvUtil.isAndroid()) {
                    driver.safeSendKeys(PIN_INPUT, WAIT_10_S, APP_PIN);
                } else if (EnvUtil.isIos()) {
                    for (int i = 1; i <= APP_PIN.length(); i++) {
                        driver.safeSendKeys(
                                PIN_INPUT.byXpath(
                                        "//XCUIElementTypeApplication[@name=\"MyPayments " +
                                                "Pre-Prod\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther" +
                                                "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther" +
                                                "/XCUIElementTypeSecureTextField[" + i + "]"),
                                WAIT_10_S,
                                String.valueOf(APP_PIN.charAt(i - 1))
                        );
                    }
                }
            } catch (Exception skipElement) {
                logInfo("Element \"" + PIN_INPUT + "\" not found -> SKIPPED");
            }
        });

        Allure.step("Verify that the app language is set to the new one after the switch, verify in-app language list after the switch (native names and labels)", () -> {
            driver.safeClick(PROFILE_BUTTON, WAIT_10_S);
            driver.safeClick(SIDEMENU_LANGUAGE, WAIT_10_S);

            EnvUtil.safeSleep(10000);

            selectedLanguageCell = lsbFlow.getLanguageCells(this.driver).get(0);
            selectedLanguageNativeName = lsbFlow.getCellLanguageNativeName(selectedLanguageCell);
            selectedLanguageLabel = lsbFlow.getCellLanguageLabel(selectedLanguageCell);

            //assert language native name
            assertEquals(
                    MpaLanguage.capitalizeText(newLanguage.nativeName(), true),
                    MpaLanguage.capitalizeText(selectedLanguageNativeName, true),
                    "[OriginalLanguageName mismatch - switch in-app language]"
            );
            //assert language label
            assertEquals(
                    MpaLanguage.capitalizeText(newLanguage.label(newLanguage), true),
                    MpaLanguage.capitalizeText(selectedLanguageLabel, true),
                    "[selectedLanguageLabel (\"default\") mismatch - switch in-app language]"
            );
        });

        Allure.step("Verify in-app language list (native names and labels)", () -> {
            List<MpaLanguage> expectedInAppLanguagesAfterSwitch = Arrays.stream(MpaLanguage.values())
                    .filter(lang -> lang.isSupportedByMpa() && !lang.equals(newLanguage))
                    .toList();

            lsbFlow.verifyInAppLanguageList(this.driver, newLanguage, expectedInAppLanguagesAfterSwitch);
        });
    }
}
