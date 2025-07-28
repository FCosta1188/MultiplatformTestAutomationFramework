package eu.nets.test.flows.VerifyCardArticles;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.MpaWidget;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.Registration.RegistrationFlow;
import eu.nets.test.flows.data.models.MpaCard;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.flows.data.shared.CardArticleData;
import eu.nets.test.util.EnvUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_BACK_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_DESCRIPTION;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_PARAGRAPH_DESCRIPTION;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_PARAGRAPH_HYPERLINK;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_PARAGRAPH_TITLE;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.CARD_ARTICLE_TITLE;
import static eu.nets.test.core.enums.MpaWidget.CARD_ICON;
import static eu.nets.test.core.enums.MpaWidget.CARD_LABEL;
import static eu.nets.test.core.enums.MpaWidget.CLOSE_CARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.NAV_BAR_ACTIVE_ITEM;
import static eu.nets.test.core.enums.MpaWidget.SECTION_TITLE;
import static eu.nets.test.core.enums.MpaWidget.TITLE;
import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyCardArticlesBaseFlow extends AbstractFlow {
    private final int WAIT_10_S = 10;
    private final int WAIT_20_S = 20;
    private final int WAIT_30_S = 30;
    private final int WAIT_40_S = 40;

    private AtomicReference<WebElement> cardTitleRef = new AtomicReference<>();
    private AtomicReference<WebElement> closeCardButtonRef = new AtomicReference<>();

    private final Set<MpaCard> COMMON_EXCLUDED_OVERVIEW_CARDS = Set.of();
    private final Set<MpaCard> COMMON_EXCLUDED_SUPPORT_CARDS = Set.of(
            CardArticleData.CARD_05_getSupport
    );
    private final Set<MpaCard> LICENCE_ACTIVATION_CARDS = Set.of(
            CardArticleData.CARD_06_NEXI_licenceActivation,
            CardArticleData.CARD_06_NETS_licenceActivation
    );

    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    protected void run(
            MpaUser user,
            MpaWidget[] appSections,
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

        Set<MpaCard> userSpecificExcludedOverviewCards = new HashSet<>(COMMON_EXCLUDED_OVERVIEW_CARDS);
        switch (user.email()) {
            case "test.nexidigital+ttpoi14@gmail.com":
                break;
            case "test.nexidigital+ios1865p1@gmail.com":
                break;
            case "test.nexidigital+puma_both@gmail.com":
                break;
            case "test.nexidigital+puma_terminal@gmail.com":
                userSpecificExcludedOverviewCards.add(CardArticleData.CARD_02_invoices);
                break;
            default:
                logInfo("No user-specific excluded support cards for user: " + user.email());
        }

        Set<MpaCard> userSpecificExcludedSupportCards = new HashSet<>(COMMON_EXCLUDED_SUPPORT_CARDS);
        switch (user.email()) {
            case "test.nexidigital+ttpoi14@gmail.com":
                break;
            case "test.nexidigital+ios1865p1@gmail.com":
                userSpecificExcludedSupportCards.add(CardArticleData.CARD_06_NEXI_licenceActivation);
                break;
            case "test.nexidigital+puma_both@gmail.com":
                break;
            case "test.nexidigital+puma_terminal@gmail.com":
                userSpecificExcludedSupportCards.add(CardArticleData.CARD_03_settlementReport);
                break;
            default:
                logInfo("No user-specific excluded support cards for user: " + user.email());
        }

        for (MpaWidget section : appSections) {
            String sectionName = section.toString().split("_")[0].toUpperCase().trim();

            Allure.step("Navigate to target section: " + section, () -> {
                driver.safeClick(section, WAIT_10_S);

                if (EnvUtil.isAndroid()) {
                    driver.waitUntilElementVisible(NAV_BAR_ACTIVE_ITEM.byAndroidXpathWithResourceIdAndAttribute("text", sectionName), WAIT_10_S);
                } else if (EnvUtil.isIos()) {
                    switch (sectionName) {
                        case "OVERVIEW" -> driver.waitUntilElementVisible(SECTION_TITLE.byIosXpathWithName("MyPayments"), WAIT_10_S);
                        case "SUPPORT" -> driver.waitUntilElementVisible(SECTION_TITLE.byIosXpathWithName("Support"), WAIT_10_S);
                        default -> throw new IllegalStateException(logError("Section not in test scope: " + sectionName));
                    }
                } else {
                    throw new UnsupportedPlatformException();
                }
            });

            EnvUtil.safeSleep(10000);

            if (sectionName.equalsIgnoreCase("OVERVIEW")) {
                for (MpaCard card : user.applicableCards()) {
                    if (userSpecificExcludedOverviewCards.contains(card)) {
                        logInfo(String.format("Card %s not applicable in section %s -> SKIPPED", card.title(), sectionName));
                        continue;
                    } else {
                        Allure.step(String.format("Verify Card and related Article: %s -> close card", card.articleTitle()), () -> {
                            verifyCardLayout(card, true);
                            cardTitleRef.get().click();
                            verifyCardArticleLayout(card);
                            verifyCardArticleText(card);

                            if (LICENCE_ACTIVATION_CARDS.contains(card)) {
                                if (user.description().contains("german_ttpoi")) {
                                    driver.waitUntilElementVisible(CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON.byIosXpathWithName("ACTIVATE YOUR LICENCE"),
                                            WAIT_10_S);
                                } else if (user.description().contains("ios_nordics_ttpoi")) {
                                    driver.waitUntilElementVisible(CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON.byIosXpathWithName("ACTIVATE NETS SOFTPOS"),
                                            WAIT_10_S);
                                } else {
                                    throw new IllegalStateException(logError("Unable to match licence activation button name for user: " + user.email()));
                                }
                            }

                            driver.safeClick(CARD_ARTICLE_BACK_BUTTON, WAIT_10_S);
                            closeCardButtonRef.get().click();
                            assertTrue(driver.waitUntilElementStaleness(cardTitleRef.get(), WAIT_10_S),
                                    "Card still displayed after closing it: " + card.title());
                        });
                    }
                }//loop cards
            } else if (sectionName.equalsIgnoreCase("SUPPORT")) {
                for (MpaCard card : user.applicableCards()) {
                    if (userSpecificExcludedSupportCards.contains(card)) {
                        logInfo(String.format("Card %s not applicable in section %s -> SKIPPED", card.title(), sectionName));
                        continue;
                    } else {
                        Allure.step(String.format("Verify Card and related Article: %s -> swipe left", card.articleTitle()), () -> {
                            int y;
                            for (y = 1; y <= 4; y++) {
                                try {
                                    verifyCardLayout(card, false);
                                    break;
                                } catch (Exception e) {
                                    logInfo("[Card] not found, trying swipe left - retry #" + y);
                                } finally {
                                    driver.swipePercent(driver.findElement(By.className("XCUIElementTypeCollectionView")), "left", 30);
                                }
                            }
                            cardTitleRef.get().click();
                            verifyCardArticleLayout(card);
                            verifyCardArticleText(card);

                            if (LICENCE_ACTIVATION_CARDS.contains(card)) {
                                if (user.description().contains("german_ttpoi")) {
                                    driver.waitUntilElementVisible(CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON.byIosXpathWithName("ACTIVATE YOUR LICENCE"),
                                            WAIT_10_S);
                                } else if (user.description().contains("ios_nordics_ttpoi")) {
                                    driver.waitUntilElementVisible(CARD_ARTICLE_SOFTPOS_ACTIVATEYOURLICENCE_BUTTON.byIosXpathWithName("ACTIVATE NETS SOFTPOS"),
                                            WAIT_10_S);
                                } else {
                                    throw new IllegalStateException(logError("Unable to match licence activation button name for user: " + user.email()));
                                }
                            }

                            driver.safeClick(CARD_ARTICLE_BACK_BUTTON, WAIT_10_S);
                        });
                    }
                }//loop cards
            } else {
                throw new IllegalStateException(logError("Section not in test scope: " + sectionName));
            }
        }//loop appSections

        if (EnvUtil.isAndroid()) {
            Allure.step("[Android specific]: save snapshot: " + user.loggedInAndroidSnapshot(), () -> {
                if (!user.loggedInAndroidSnapshot().exists()) {
                    user.loggedInAndroidSnapshot().save();
                }
            });
        }
    }//run

    private void verifyCardLayout(MpaCard card, boolean cardHasCloseButton) {
        //TODO: implement color recognition in MpaDriver
        //assertTrue(driver.getColor(CARD_CONTAINER.byXpathWithIdAndIndex(1, true), WAIT_10_S).equals(CARD_CONTAINER.getColor()));
        //TODO: implement image recognition in MpaDriver
        //assertTrue(driver.matchElementImage(cardIcon, card.iconRefImgPath(), WAIT_10_S), String.format("\"%s\" card icon does not match expected icon
        // \"%s\"", card.title(), card.iconRefImgPath()));

        WebElement cardIcon;
        if (EnvUtil.isAndroid()) {
            cardTitleRef.set(driver.waitUntilElementVisible(TITLE.byAndroidXpathWithResourceIdAndAttribute("text", card.title()), WAIT_10_S));
            assertTrue(driver.waitUntilElementVisible(CARD_LABEL.byAndroidXpathWithResourceIdAndAttribute("text", card.label()), WAIT_10_S).isDisplayed(),
                    "Card label not diplayed");
            try {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byAndroidXpathWithResourceIdAndIndex(1, true), WAIT_10_S);
            } catch (TimeoutException | NoSuchElementException retryWithoutGroupSelector) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byAndroidXpathWithResourceIdAndIndex(1, false), WAIT_10_S);
            }
        } else if (EnvUtil.isIos()) {
            cardTitleRef.set(driver.waitUntilElementVisible(TITLE.byIosXpathWithName(card.title()), WAIT_10_S));
            assertTrue(driver.waitUntilElementVisible(CARD_LABEL.byIosXpathWithName(card.label()), WAIT_10_S).isDisplayed(), "Card label not diplayed");
            if (card.label().equalsIgnoreCase("Transactions")) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_transaction"), WAIT_10_S);
            } else if (card.label().equalsIgnoreCase("Nexi SoftPOS") || card.label().equalsIgnoreCase("Nets SoftPOS")) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_tapToPay"), WAIT_10_S);
            } else {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_" + card.label().toLowerCase()), WAIT_10_S);
            }
        } else {
            throw new UnsupportedPlatformException();
        }
        assertTrue(cardTitleRef.get().isDisplayed(), "Card not displayed");
        assertTrue(cardIcon.isDisplayed(), "Card icon not diplayed");

        if (cardHasCloseButton) {
            closeCardButtonRef.set(driver.waitUntilElementVisible(CLOSE_CARD_BUTTON, WAIT_10_S));
            assertTrue(closeCardButtonRef.get().isDisplayed(), "Card close button not diplayed");
        }
    }

    private void verifyCardArticleLayout(MpaCard card) {
        //TODO: implement color recognition in MpaDriver
        //assertTrue(driver.getColor(CARD_CONTAINER.byXpathWithIdAndIndex(1, true), WAIT_10_S).equals(CARD_CONTAINER.getColor()));
        //TODO: implement image recognition in MpaDriver
        //assertTrue(driver.matchElementImage(cardIcon, card.iconRefImgPath(), WAIT_10_S), String.format("\"%s\" card icon does not match expected icon
        // \"%s\"", card.title(), card.iconRefImgPath()));

        WebElement cardIcon;
        if (EnvUtil.isAndroid()) {
            assertTrue(driver.waitUntilElementVisible(CARD_LABEL.byAndroidXpathWithResourceIdAndAttribute("text", card.label()), WAIT_10_S).isDisplayed(),
                    "Card label not diplayed");
            try {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byAndroidXpathWithResourceIdAndIndex(1, true), WAIT_10_S);
            } catch (TimeoutException | NoSuchElementException retryWithoutGroupSelector) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byAndroidXpathWithResourceIdAndIndex(1, false), WAIT_10_S);
            }
        } else if (EnvUtil.isIos()) {
            assertTrue(driver.waitUntilElementVisible(CARD_LABEL.byIosXpathWithName(card.label()), WAIT_10_S).isDisplayed(), "Card label not diplayed");
            if (card.label().equalsIgnoreCase("Transactions")) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_transaction"), WAIT_10_S);
            } else if (card.label().equalsIgnoreCase("Nexi SoftPOS") || card.label().equalsIgnoreCase("Nets SoftPOS")) {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_tapToPay"), WAIT_10_S);
            } else {
                cardIcon = driver.waitUntilElementVisible(CARD_ICON.byIosXpathWithName("article_" + card.label().toLowerCase()), WAIT_10_S);
            }
        } else {
            throw new UnsupportedPlatformException();
        }
        assertTrue(cardIcon.isDisplayed(), "Card icon not diplayed");
    }

    private void verifyCardArticleText(MpaCard card) {
        WebElement cardArticle;
        if (EnvUtil.isAndroid()) {
            cardArticle = driver.waitUntilElementVisible(CARD_ARTICLE, WAIT_10_S);
            assertTrue(driver.waitUntilElementVisible(CARD_ARTICLE_TITLE.byAndroidXpathWithResourceIdAndAttribute("text", card.articleTitle()),
                    WAIT_10_S).isDisplayed(), "Article title not displayed");
            assertTrue(driver.waitUntilElementVisible(CARD_ARTICLE_DESCRIPTION.byAndroidXpathWithResourceIdAndAttribute("text", card.articleDescription()),
                    WAIT_10_S).isDisplayed(), "Article description not displayed");
        } else if (EnvUtil.isIos()) {
            cardArticle = driver.waitUntilElementVisible(CARD_ARTICLE.byXpath("//XCUIElementTypeScrollView/XCUIElementTypeOther[1]"), WAIT_10_S);
            assertTrue(driver.waitUntilElementVisible(CARD_ARTICLE_TITLE.byIosXpathWithName(card.articleTitle()), WAIT_10_S).isDisplayed(),
                    "Article title not displayed");
            assertTrue(driver.waitUntilElementVisible(CARD_ARTICLE_DESCRIPTION.byIosXpathWithName(card.articleDescription()), WAIT_10_S).isDisplayed(),
                    "Article description not displayed");
        } else {
            throw new UnsupportedPlatformException();
        }

        if (card.paragraphTitle1() != null && !card.paragraphTitle1().isBlank()) {
            verifyArticleParagraph(
                    cardArticle, card.paragraphTitle1(), card.paragraphDescription1(), card.paragraphHyperlink1(), 1
            );
        }
        if (card.paragraphTitle2() != null && !card.paragraphTitle2().isBlank()) {
            verifyArticleParagraph(
                    cardArticle, card.paragraphTitle2(), card.paragraphDescription2(), card.paragraphHyperlink2(), 2
            );
        }
        if (card.paragraphTitle3() != null && !card.paragraphTitle3().isBlank()) {
            verifyArticleParagraph(
                    cardArticle, card.paragraphTitle3(), card.paragraphDescription3(), card.paragraphHyperlink3(), 3
            );
        }
        if (card.paragraphTitle4() != null && !card.paragraphTitle4().isBlank()) {
            verifyArticleParagraph(
                    cardArticle, card.paragraphTitle4(), card.paragraphDescription4(), card.paragraphHyperlink4(), 4
            );
        }
        if (card.paragraphTitle5() != null && !card.paragraphTitle5().isBlank()) {
            verifyArticleParagraph(
                    cardArticle, card.paragraphTitle5(), card.paragraphDescription5(), card.paragraphHyperlink5(), 5
            );
        }
    }

    private void verifyArticleParagraph(
            WebElement cardArticle,
            String expectedTitle,
            String expectedDescription,
            String expectedHyperlink,
            int paragraphIndex
    ) {
        if (EnvUtil.isAndroid()) {
            String androidActualTitle = "";
            String androidActualDescription = "";

            int i;
            for (i = 1; i <= 3; i++) {
                driver.scrollPercent(cardArticle, "down", 5);

                try {
                    androidActualTitle = driver.waitUntilElementVisible(CARD_ARTICLE_PARAGRAPH_TITLE.byAndroidXpathWithResourceIdAndIndex(paragraphIndex, true),
                            WAIT_10_S).getText();
                    androidActualDescription = driver.waitUntilElementVisible(CARD_ARTICLE_PARAGRAPH_DESCRIPTION.byAndroidXpathWithResourceIdAndIndex(
                            paragraphIndex,
                            true), WAIT_10_S).getText();
                    break;
                } catch (NoSuchElementException e) {
                    logInfo("[Paragraph title/description] not found, trying scroll down 5% - retry #" + i);
                }
            }

            if (androidActualTitle.isBlank() || androidActualDescription.isBlank()) {
                throw new IllegalStateException(
                        String.format(
                                "[Paragraph #%d]: actual title or description not found or blank after %d scroll attempts." +
                                        "\n\t- Actual title: <%s>" +
                                        "\n\t- Expected title: <%s>" +
                                        "\n\t- Actual description: <%s>" +
                                        "\n\t- Expected description: <%s>",
                                paragraphIndex, i, androidActualTitle, expectedTitle, androidActualDescription, expectedDescription)
                );
            }

            assertEquals(
                    expectedTitle,
                    androidActualTitle,
                    "[Paragraph title]: text mismatch"
            );

            expectedDescription = expectedHyperlink != null && !expectedHyperlink.isBlank()
                    ? expectedDescription + "\n\n" + expectedHyperlink
                    : expectedDescription;
            assertEquals(
                    expectedDescription,
                    androidActualDescription,
                    "[Paragraph description]: text mismatch"
            );
        } else if (EnvUtil.isIos()) {
            boolean elementsFound = false;
            int j;
            for (j = 1; j <= 3; j++) {
                driver.swipePercent(driver.findElement(By.className("XCUIElementTypeScrollView")), "up", 20);

                try {
                    driver.waitUntilElementVisible(CARD_ARTICLE_PARAGRAPH_TITLE.byIosXpathWithName(expectedTitle), WAIT_10_S);
                    driver.waitUntilElementVisible(CARD_ARTICLE_PARAGRAPH_DESCRIPTION.byIosXpathWithName(expectedDescription), WAIT_10_S);
                    if (expectedHyperlink != null && !expectedHyperlink.isBlank()) {
                        driver.waitUntilElementVisible(CARD_ARTICLE_PARAGRAPH_HYPERLINK.byIosXpathWithName(expectedHyperlink), WAIT_10_S);
                    }
                    elementsFound = true;
                    break;
                } catch (NoSuchElementException e) {
                    logInfo("[Paragraph title/description] not found, trying swipe up 20% - retry #" + j);
                }
            }

            if (!elementsFound) {
                throw new IllegalStateException(
                        String.format(
                                "[Paragraph #%d]: actual title or description not found after %d swipe attempts." +
                                        "\n\t- Expected title: <%s>" +
                                        "\n\t- Expected description: <%s>" +
                                        "\n\t- Expected hyperlink: <%s>",
                                paragraphIndex, j, expectedTitle, expectedDescription, expectedHyperlink)
                );
            }
        } else {
            throw new UnsupportedPlatformException();
        }
    }
}//class