package eu.nets.test.flows.LanguageSwitch;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.drivers.MpaDriver;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static eu.nets.test.core.enums.MpaWidget.APP_LANGUAGE_LABEL;
import static eu.nets.test.core.enums.MpaWidget.ORIGINAL_LANGUAGE_LABEL;
import static eu.nets.test.core.enums.MpaWidget.PROFILE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SIDEMENU_LANGUAGE;
import static eu.nets.test.core.enums.MpaWidget.LANGUAGE_LIST_CONTAINER;
import static eu.nets.test.util.AllureUtil.logInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageSwitchBaseFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return AndroidSnapshot.NO_MPA;
    }

    protected void run(
            MpaUser user,
            MpaLanguage desiredAppLanguage,
            MpaLanguage selectedAppLanguage,
            List<MpaLanguage> expectedInAppLanguages
    ) throws IOException, InterruptedException {
        Allure.step("Launch driver and set system language: " + desiredAppLanguage, () -> {
            if (desiredAppLanguage != null) {
                launchDriver(false, desiredAppLanguage.getLanguage(), desiredAppLanguage.getCountry());
            }
        });

        Allure.step("run RegistrationFlow - user: " + user, () -> {
            RegistrationFlow rFlow = new RegistrationFlow();
            rFlow.setDriver(this.driver);
            rFlow.run(user, desiredAppLanguage, false, false);
            this.setDriver(rFlow.getDriver());
        });

        Allure.step("Verify that the app language is set correctly (desired or fallback), verify in-app language list (native names and labels)", () -> {
            driver.safeClick(PROFILE_BUTTON, WAIT_10_S);
            driver.safeClick(SIDEMENU_LANGUAGE, WAIT_10_S);

            verifyInAppLanguageList(this.driver, selectedAppLanguage, expectedInAppLanguages);
        });
    }

    public List<WebElement> getLanguageCells(MpaDriver d) {
        if(EnvUtil.isAndroid()) {
            return new ArrayList<>(d.findElements(By.xpath("//android.widget.GridView[@resource-id=\"eu.nets.mypayments.mock:id/recycler_view\"]/android.widget.FrameLayout")));
        } else if (EnvUtil.isIos()) {
            return new ArrayList<>(d.findElements(By.xpath("//XCUIElementTypeTable[@name=\"change_lamguage_table_view\"]/XCUIElementTypeCell")));
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public String getCellLanguageNativeName(WebElement cell) {
        if(EnvUtil.isAndroid()) {
            return cell.findElement(ORIGINAL_LANGUAGE_LABEL.byAndroidResourceId()).getAttribute("text");
        } else if (EnvUtil.isIos()) {
            List<WebElement> cellLanguageNames = cell.findElements(By.xpath("//XCUIElementTypeStaticText"));
            return cellLanguageNames.get(1).getAttribute("name");
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public String getCellLanguageLabel(WebElement cell) {
        if(EnvUtil.isAndroid()) {
            return cell.findElement(APP_LANGUAGE_LABEL.byAndroidResourceId()).getAttribute("text");
        } else if (EnvUtil.isIos()) {
            List<WebElement> cellLanguageNames = cell.findElements(By.xpath("//XCUIElementTypeStaticText"));
            return cellLanguageNames.get(0).getAttribute("name");
        } else {
            throw new UnsupportedPlatformException();
        }
    }

    public void verifyInAppLanguageList(MpaDriver d, MpaLanguage selectedAppLanguage, List<MpaLanguage> expectedInAppLanguages) {
        List<String> expectedInAppLanguagesNativeNames = new ArrayList<>();
        for(MpaLanguage lang : expectedInAppLanguages) {
            expectedInAppLanguagesNativeNames.add(lang.capitalizeDictionaryEntry(lang.toString().toLowerCase(), true));
        }
        expectedInAppLanguagesNativeNames.sort(Comparator.naturalOrder());
        List<String> languagesToBeVerified = new ArrayList<>(expectedInAppLanguagesNativeNames);

        List<WebElement> languageCells = getLanguageCells(d);
        for (int i = 0; i < languageCells.size(); i++) {
            WebElement cell = languageCells.get(i);

            String cellLanguageNativeName = getCellLanguageNativeName(cell); //name of the language as written natively
            if(cellLanguageNativeName.contains("(")) {
                cellLanguageNativeName = cellLanguageNativeName.split("\\(")[0].trim();
            }

            String cellLanguageLabel = getCellLanguageLabel(cell); //name of the language translated in the language selected in the app, or "default" value for first entry (i.e.: selected language)
            if(cellLanguageLabel.contains("(")) {
                cellLanguageLabel = cellLanguageLabel.split("\\(")[0].trim();
            }

            if(i == 0) {
                //The first cell must contain the current app language, which should match the selected language (if supported by MPA) or the default one (when the selected language is not supported by MPA)
                //assert language native name
                assertEquals(
                        selectedAppLanguage.capitalizeDictionaryEntry(selectedAppLanguage.toString().toLowerCase(), true),
                        cellLanguageNativeName,
                        "[cell #0: language native name mismatch - switch device language]"
                );
                //assert language label
                assertEquals(
                        MpaLanguage.capitalizeText(selectedAppLanguage.label(selectedAppLanguage), true),
                        cellLanguageLabel,
                        "[cell #0: language label (\"default\") mismatch - switch device language]"
                );
            } else {
                //The rest of the cells must contain the languages supported by MPA, shown in alphabetical order (order based on language native names)
                MpaLanguage languageToBeVerified = MpaLanguage.fromNativeName(expectedInAppLanguagesNativeNames.get(i - 1));
                //assert language native name
                assertEquals(
                        MpaLanguage.capitalizeText(languageToBeVerified.nativeName(), true),
                        cellLanguageNativeName,
                        "[cell #" + i + ": language native name mismatch - switch device language] expectedInAppLanguagesNativeNames: " + expectedInAppLanguagesNativeNames
                );
                //assert language label
                assertEquals(
                        MpaLanguage.capitalizeText(languageToBeVerified.label(selectedAppLanguage), true),
                        cellLanguageLabel,
                        "[cell #" + i + ": language label mismatch - switch device language] expectedInAppLanguagesNativeNames: " + expectedInAppLanguagesNativeNames
                );

                languagesToBeVerified.remove(MpaLanguage.capitalizeText(languageToBeVerified.nativeName(), true));
            }
        }

        if(EnvUtil.isAndroid()) {
            d.scrollPercent(d.findElement(LANGUAGE_LIST_CONTAINER.byAndroidResourceId()), "down", 100);
            logInfo("Scrolling down the language list to verify all languages.");

            languageCells = getLanguageCells(d);
            for (int j = 0; j < languageCells.size(); j++) {
                if(j >= languagesToBeVerified.size()) {
                    break; //no more languages to verify
                }

                WebElement cell = languageCells.get(j);

                String cellLanguageNativeName = getCellLanguageNativeName(cell); //name of the language as written natively
                if(cellLanguageNativeName.contains("(")) {
                    cellLanguageNativeName = cellLanguageNativeName.split("\\(")[0].trim();
                }

                String cellLanguageLabel = getCellLanguageLabel(cell); //name of the language translated in the language selected in the app, or "default" value for first entry (i.e.: selected language)
                if(cellLanguageLabel.contains("(")) {
                    cellLanguageLabel = cellLanguageLabel.split("\\(")[0].trim();
                }

                //verify languages not yet verified before scrolling down (or swiping up)
                if(languagesToBeVerified.get(j).equalsIgnoreCase(cellLanguageNativeName)){
                    MpaLanguage languageToBeVerified = MpaLanguage.fromNativeName(languagesToBeVerified.get(j));
                    //assert language native name
                    assertEquals(
                            MpaLanguage.capitalizeText(languageToBeVerified.nativeName(), true),
                            cellLanguageNativeName,
                            "[cell #" + j + ": language native name mismatch - switch device language] expectedInAppLanguagesNativeNames: " + expectedInAppLanguagesNativeNames
                    );
                    //assert language label
                    assertEquals(
                            MpaLanguage.capitalizeText(languageToBeVerified.label(selectedAppLanguage), true),
                            cellLanguageLabel,
                            "[cell #" + j + ": language label mismatch - switch device language] expectedInAppLanguagesNativeNames: " + expectedInAppLanguagesNativeNames
                    );
                }
            }
        }
    }
}
