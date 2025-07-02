package eu.nets.test.flows.LanguageSwitch;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static eu.nets.test.core.enums.MpaWidget.APP_LANGUAGE_LABEL;
import static eu.nets.test.core.enums.MpaWidget.ORIGINAL_LANGUAGE_LABEL;
import static eu.nets.test.core.enums.MpaWidget.PROFILE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SIDEMENU_LANGUAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageSwitchDeviceLanguageNotSupportedByMpaFlow extends AbstractFlow {
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
        return "LanguageSwitchDeviceLanguageNotSupportedByMpaFlow";
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}")
    @MethodSource("eu.nets.test.flows.data.LanguageSwitch.LanguageSwitchData#streamDeviceLanguageNotSupportedByMpa")
    @Epic("Language Switch")
    @Feature("https://nexigroup-germany.atlassian.net/browse/MSA-6597")
    @Story("LanguageSwitchDeviceLanguageNotSupportedByMpa")
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
        Allure.step("Launch driver and set system language: " + appLanguage, () -> {
            if (appLanguage != null) {
                launchDriver(false, appLanguage.getLanguage(), appLanguage.getCountry());
            }
        });

        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: load logged in snapshot: " + user.loggedInAndroidSnapshot(), () -> {
                if (user.loggedInAndroidSnapshot().exists()) {
                    user.loggedInAndroidSnapshot().load();
                }
            });
        } else if ((EnvUtil.isAndroid() && !user.loggedInAndroidSnapshot().exists()) || EnvUtil.isIos()) {
            Allure.step("run RegistrationFlow - user: " + user, () -> {
                RegistrationFlow rFlow = new RegistrationFlow();
                rFlow.setDriver(this.driver);
                rFlow.run(user, appLanguage, false, false);
                this.setDriver(rFlow.getDriver());
            });
        } else {
            throw new UnsupportedPlatformException();
        }

        Allure.step("Verify that the app language is set to the default one, verify Language page layout: ", () -> {
            driver.safeClick(PROFILE_BUTTON, WAIT_10_S);
            driver.safeClick(SIDEMENU_LANGUAGE, WAIT_10_S);
            //driver.waitUntilElementVisible(LANGUAGE_PAGE_TITLE, WAIT_20_S);

            List<WebElement> languageCells;
            if(EnvUtil.isAndroid()) {
                languageCells = driver.findElements(By.xpath("//android.widget.TextView[@text='Default']"));
            } else if (EnvUtil.isIos()) {
                languageCells = driver.findElements(By.xpath("//XCUIElementTypeTable[@name=\"change_lamguage_table_view\"]/XCUIElementTypeCell"));
            } else {
                throw new UnsupportedPlatformException();
            }

            for (int i = 0; i < languageCells.size(); i++) {
                WebElement cell = languageCells.get(i);
                String cellOriginalLanguageName; //name of the language as written in the language
                String cellAppLanguageName; //name of the language translated in the language selected in the app

                if(EnvUtil.isAndroid()) {
                    cellOriginalLanguageName = cell.findElement(ORIGINAL_LANGUAGE_LABEL.byAndroidResourceId()).getAttribute("text");
                    cellAppLanguageName = cell.findElement(APP_LANGUAGE_LABEL.byAndroidResourceId()).getAttribute("text");
                } else if (EnvUtil.isIos()) {
                    List<WebElement> cellTexts = cell.findElements(By.xpath("//XCUIElementTypeStaticText"));
                    cellOriginalLanguageName = cellTexts.get(0).getAttribute("name");
                    cellAppLanguageName = cellTexts.get(1).getAttribute("name");
                } else {
                    throw new UnsupportedPlatformException();
                }

                //The first cell must contain the current app language, which should match the default language, since the device language is not supported by MPA
                if(i == 0) {
                    assertEquals(
                            MpaLanguage.DEFAULT.toString().toLowerCase(),
                            cellOriginalLanguageName.toLowerCase(),
                            "[cellOriginalLanguageName mismatch]"
                    );
                    assertEquals(
                            "default",
                            cellAppLanguageName.toLowerCase(),
                            "[cellAppLanguageName mismatch]"
                    );
                } else {
                    //The rest of the cells must contain the languages supported by MPA, shown in alphabetical order (order based on the current app language)
                    List<MpaLanguage> mpaSupportedLanguages = Arrays.stream(MpaLanguage.values()).filter(lang -> lang.isSupportedByMpa() && !lang.equals(MpaLanguage.DEFAULT)).toList();
                    List<String> expectedDisplayedOriginalLanguageNames = new ArrayList<>();
                    for(MpaLanguage language : mpaSupportedLanguages) {
                        expectedDisplayedOriginalLanguageNames.add(language.getDictionary().get(language.toString().toLowerCase()));
                    }
                    expectedDisplayedOriginalLanguageNames.sort(Comparator.naturalOrder());

                    assertEquals(
                            expectedDisplayedOriginalLanguageNames.get(i - 1).toLowerCase(),
                            cellOriginalLanguageName.toLowerCase(),
                            "[Language mismatch]"
                    );
                }
            }
        });
    }
}
