package eu.nets.test.flows.data.shared;

import eu.nets.test.core.enums.PathKey;
import eu.nets.test.flows.data.models.MpaCard;
import eu.nets.test.util.PropertiesUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CardArticleData {
    private static final Path IMG_DIR = PathKey.IMG.resolve().asPath();

    public static final MpaCard CARD_01_NEXI_appDataVisible = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.label.appDataVisible.nexi"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.appDataVisible.nexi")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.appDataVisible.nexi"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.appDataVisible.nexi"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.appDataVisible.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.appDataVisible.nexi"),
            null,
            null, null, null, null, null, null
    );

    public static final MpaCard CARD_01_NETS_appDataVisible = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.label.appDataVisible.nets"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.appDataVisible.nets")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.appDataVisible.nets"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.appDataVisible.nets"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.appDataVisible.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.appDataVisible.nets"),
            null,
            null, null, null, null, null, null
    );

    public static final MpaCard CARD_02_invoices = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.invoices"),
            PropertiesUtil.MPA.getProperty("card.label.invoices"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.invoices")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.invoices"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.invoices"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.invoices"),
            null,
            null,
            null, null,
            null,
            null, null, null, null, null
    );

    public static final MpaCard CARD_02_invoices_statement = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.invoices"),
            PropertiesUtil.MPA.getProperty("card.label.invoices"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.invoices")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.invoices"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.invoices").replace("settlement", "statement"),
            null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.invoices"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.invoices"),
            null,
            null,
            null, null,
            null,
            null, null, null, null, null
    );

    public static final MpaCard CARD_03_settlementReport = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.label.settlementReport"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.settlementReport")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.settlementReport"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.settlementReport"), null,
            null,
            null, null,
            null,
            null, null, null, null, null
    );

    public static final MpaCard CARD_03_statementReport = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.settlementReport").replace("settlement", "statement"),
            PropertiesUtil.MPA.getProperty("card.label.settlementReport"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.settlementReport")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.settlementReport").replace("settlement", "statement"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.settlementReport").replace("settlement", "statement"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.settlementReport").replace("settlement", "statement"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.settlementReport").replace("settlement", "statement"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.settlementReport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.settlementReport").replace("settlement", "statement"), null,
            null,
            null, null,
            null,
            null, null, null, null, null);

    public static final MpaCard CARD_04_gettingStartedWithTerminal = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.gettingStartedWithTerminal"),
            PropertiesUtil.MPA.getProperty("card.label.gettingStartedWithTerminal"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.gettingStartedWithTerminal")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.gettingStartedWithTerminal"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.gettingStartedWithTerminal"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.gettingStartedWithTerminal"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.gettingStartedWithTerminal"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphHyperlink1.gettingStartedWithTerminal"),
            null,
            null, null,
            null,
            null, null,
            null,
            null, null, null, null, null);

    public static final MpaCard CARD_05_getSupport = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.getSupport"),
            PropertiesUtil.MPA.getProperty("card.label.getSupport"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.getSupport")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.getSupport"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.getSupport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.getSupport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.getSupport"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphHyperlink1.getSupport"),
            null, null, null, null, null, null, null, null, null, null, null, null
    );

    public static final MpaCard CARD_06_NEXI_licenceActivation = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.label.licenceActivation.nexi"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.licenceActivation.nexi")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.licenceActivation.nexi"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.licenceActivation.nexi"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.licenceActivation.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.licenceActivation.nexi"), null,
            null, null, null, null, null, null
    );

    public static final MpaCard CARD_06_NETS_licenceActivation = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.label.licenceActivation.nets"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.licenceActivation.nets")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.licenceActivation.nets"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.licenceActivation.nets"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.licenceActivation.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.licenceActivation.nets"), null,
            null, null, null, null, null, null
    );

    public static final MpaCard CARD_07_NEXI_gettingStartedWithNexiSoftPOS = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.label.gettingStartedWithNexiSoftPOS.nexi"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.gettingStartedWithNexiSoftPOS.nexi")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphHyperlink1.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.gettingStartedWithNexiSoftPOS.nexi"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.gettingStartedWithNexiSoftPOS.nexi"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.gettingStartedWithNexiSoftPOS.nexi"), null,
            null, null, null, null, null, null
    );

    public static final MpaCard CARD_07_NETS_gettingStartedWithNexiSoftPOS = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.label.gettingStartedWithNexiSoftPOS.nets"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.gettingStartedWithNexiSoftPOS.nets")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphHyperlink1.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.gettingStartedWithNexiSoftPOS.nets"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.gettingStartedWithNexiSoftPOS.nets"),
            null,
            null, null, null, null, null, null, null, null, null
    );

    public static final MpaCard CARD_08_yourOrder = new MpaCard(
            PropertiesUtil.MPA.getProperty("card.title.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.label.yourOrder"),
            IMG_DIR.resolve(PropertiesUtil.MPA.getProperty("card.icon.yourOrder")),
            null,
            PropertiesUtil.MPA.getProperty("card.articleTitle.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.articleDescription.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle1.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription1.yourOrder"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle2.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription2.yourOrder"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle3.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription3.yourOrder"), null,
            PropertiesUtil.MPA.getProperty("card.article.paragraphTitle4.yourOrder"),
            PropertiesUtil.MPA.getProperty("card.article.paragraphDescription4.yourOrder"), null, null, null, null);

    public static List<MpaCard> allCards() {
        return new ArrayList<>(List.of(
                CARD_01_NEXI_appDataVisible,
                CARD_01_NETS_appDataVisible,
                CARD_02_invoices,
                CARD_02_invoices_statement,
                CARD_03_settlementReport,
                CARD_03_statementReport,
                CARD_04_gettingStartedWithTerminal,
                CARD_05_getSupport,
                CARD_06_NEXI_licenceActivation,
                CARD_06_NETS_licenceActivation,
                CARD_07_NEXI_gettingStartedWithNexiSoftPOS,
                CARD_07_NETS_gettingStartedWithNexiSoftPOS,
                CARD_08_yourOrder
        ));
    }
}
