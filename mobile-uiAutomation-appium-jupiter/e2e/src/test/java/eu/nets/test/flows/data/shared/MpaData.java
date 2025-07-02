package eu.nets.test.flows.data.shared;

import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.PathKey;
import eu.nets.test.flows.data.models.MpaCard;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.PropertiesUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MpaData {
    public static final Path IMG_DIR = PathKey.IMG.resolve().asPath();

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
            null, null, null, null, null);

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
                CARD_02_invoices,
                CARD_03_settlementReport,
                CARD_04_gettingStartedWithTerminal,
                CARD_05_getSupport,
                CARD_06_NEXI_licenceActivation,
                CARD_06_NETS_licenceActivation,
                CARD_07_NEXI_gettingStartedWithNexiSoftPOS,
                CARD_07_NETS_gettingStartedWithNexiSoftPOS,
                CARD_08_yourOrder
        ));
    }

    private static List<MpaCard> includeCards(String... cardPrefixes) {
        return Arrays.stream(MpaData.class.getDeclaredFields())
                .filter(field -> {
                    String fieldName = field.getName();
                    return fieldName.startsWith("CARD_") && Arrays.stream(cardPrefixes).anyMatch(fieldName::startsWith);
                })
                .map(field -> {
                    try {
                        return (MpaCard) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<MpaCard> excludeCards(String... cardPrefixes) {
        return Arrays.stream(MpaData.class.getDeclaredFields())
                .filter(field -> {
                    String fieldName = field.getName();
                    return fieldName.startsWith("CARD_") && Arrays.stream(cardPrefixes).noneMatch(fieldName::startsWith);
                })
                .map(field -> {
                    try {
                        return (MpaCard) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    // outlets = sales locations
    public static List<MpaUser> allUsers() {
        return new ArrayList<>(List.of(
                new MpaUser(
                        "SMP",
                        "standard, SmartPay Blue Portal, 1 outlet",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.smp12pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP,
                        Set.of("RegistrationFlow", "VerifyCardArticlesSMPFlow", "VerifyTerminalSupportSMPGermanFlow", "LanguageSwitchFlow")
                ),
                new MpaUser(
                        "SMP",
                        "standard, SmartPay Blue Portal, 1 outlet, Receipt_Tip_True",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.smp23pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.23pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_23PP,
                        Set.of("RegistrationFlow", "VerifyCardArticlesSMPFlow", "VerifyTerminalSupportSMPGermanFlow")
                ),
                new MpaUser(
                        "SMP_CH",
                        "standard, swiss_terminal, android_licence ready for activation, Swiss_SMP",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.ttpoa_ch_term.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_ch"),
                        includeCards("CARD_01_NEXI",
                                "CARD_02",
                                "CARD_03",
                                "CARD_04",
                                "CARD_05",
                                "CARD_06_NEXI_licenceActivation",
                                "CARD_07_NEXI_gettingStartedWithNexiSoftPOS"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_CH,
                        Set.of("RegistrationFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_terminal, android_licence ready for activation",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.12months26.gmail"),
                        null,
                        // This account will see Tap to Pay related cards with "Nexi SoftPOS" on Android
                        includeCards("CARD_01_NEXI",
                                "CARD_02",
                                "CARD_03",
                                "CARD_04",
                                "CARD_05",
                                "CARD_06_NEXI_licenceActivation",
                                "CARD_07_NEXI_gettingStartedWithNexiSoftPOS"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_ANDROID_LICENCE,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "TapToPay_Android_Nexi", "VerifyCardArticlesSMPFlow")
                ),
                new MpaUser(
                        "SMP",
                        "1 < outlets < 20",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.a12m.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_CH,
                        Set.of("VerifyCardArticlesSMPFlow")
                ),
                new MpaUser(
                        "SMP",
                        "one company",
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        PropertiesUtil.MPA.getProperty("vat.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("ads.smp12pp"),
                        PropertiesUtil.MPA.getProperty("country.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("mail.ads.smp12pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP,
                        Set.of("VerifyCardArticlesSMPFlow", "VerifyMyInfoFlow")
                ),
                new MpaUser(
                        "BAU",
                        "standard, DACH BAU Portal (Orange Portal), 20+ outlets, BAU=MyCC",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.bau.gmail"),
                        PropertiesUtil.MPA.getProperty("org.bau"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_BAU,
                        Set.of("RegistrationFlow", "VerifyCardArticlesBAUFlow")
                ),
                new MpaUser(
                        "TNP",
                        "standard, MyNets 1.0 Portal",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.tnp11.gmail"),
                        PropertiesUtil.MPA.getProperty("org.tnp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_TNP11,
                        Set.of("RegistrationFlow", "VerifyCardArticlesTNPFlow")
                ),
                new MpaUser(
                        "TNP",
                        "no phone number",
                        PropertiesUtil.MPA.getProperty("fullName.tnp11"),
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.tnp11.gmail"),
                        PropertiesUtil.MPA.getProperty("org.tnp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_TNP11,
                        Set.of("VerifyCardArticlesTNPFlow", "VerifyMyInfoFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "standard, MyNets 2.0 Portal",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma23874pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.puma.standard"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05", "CARD_08"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA,
                        Set.of("RegistrationFlow", "VerifyCardArticlesPUMAFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "terminal_only",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma.yahoo"),
                        PropertiesUtil.MPA.getProperty("org.puma.terminalOnly"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_TERMINAL_ONLY,
                        Set.of("VerifyCardArticlesPUMAFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "acquiring_only",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma950.gmail"),
                        PropertiesUtil.MPA.getProperty("org.puma.acquiringOnly"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_ACQUIRING_ONLY,
                        Set.of("VerifyCardArticlesPUMAFlow", "VerifyOverviewPumaElementsFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "multiple companies",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma950.gmail"),
                        PropertiesUtil.MPA.getProperty("orgs.puma950"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_ACQUIRING_ONLY,
                        Set.of("VerifyCardArticlesPUMAFlow", "VerifyMyInfoFlow")
                ),
                new MpaUser(
                        "MEPO",
                        "standard, Merchant Portal FI (IST/MAS)",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.mepo.gmail"),
                        PropertiesUtil.MPA.getProperty("org.mepo"),
                        includeCards("CARD_02", "CARD_03", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_MEPO,
                        Set.of("RegistrationFlow", "VerifyCardArticlesMEPOFlow")
                ),
                new MpaUser(
                        "CVR",
                        "single admin",
                        null,
                        PropertiesUtil.MPA.getProperty("vat.cvr.singleAdmin"),
                        null,
                        PropertiesUtil.MPA.getProperty("country.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("email.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("org.cvr.singleAdmin"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_CVR_SINGLE_ADMIN,
                        Set.of("RegistrationCvrFlow")
                ),
                new MpaUser(
                        "CVR",
                        "multiple admins",
                        null,
                        PropertiesUtil.MPA.getProperty("vat.cvr.multipleAdmins"),
                        null,
                        PropertiesUtil.MPA.getProperty("country.cvr.multipleAdmins"),
                        PropertiesUtil.MPA.getProperty("email.cvr.multipleAdmins"),
                        PropertiesUtil.MPA.getProperty("org.cvr.multipleAdmins"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_CVR_MULTIPLE_ADMINS,
                        Set.of("RegistrationCvrFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_ttpoi, ios_licence, no_physical_terminal",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.ttpoi14.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_ttpoi14"),
                        //This account will see Tap to Pay related cards with "Nexi SoftPOS" on iOS
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_05", "CARD_06_NEXI", "CARD_07_NEXI"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_IOS_LICENCE_TTPOI14,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "VerifyCardArticlesTapToPayFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_ttpoi, ios_licence, no_physical_terminal",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.ios1865p1.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_1865p1"),
                        //This account will see Tap to Pay related cards with "Nexi SoftPOS" on iOS
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03", "CARD_05", "CARD_06_NEXI", "CARD_07_NEXI"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_IOS_LICENCE_1865P1,
                        Set.of("VerifyCardArticlesTapToPayFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "ios_nordics_ttpoi, puma_both",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma_both"),
                        PropertiesUtil.MPA.getProperty("org.puma.both"),
                        // This account will see Tap to Pay related card with "Nets SoftPOS" on iOS
                        includeCards("CARD_01_NETS", "CARD_02", "CARD_03", "CARD_04", "CARD_05", "CARD_06_NETS", "CARD_07_NETS"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_BOTH,
                        Set.of("VerifyCardArticlesTapToPayFlow", "VerifyOverviewPumaElementsFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "ios_nordics_ttpoi, puma_terminal_only",
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma_terminal_only"),
                        PropertiesUtil.MPA.getProperty("org.puma.terminalOnly_FI"),
                        // This account will see Tap to Pay related card with "Nets SoftPOS" on iOS
                        includeCards("CARD_01_NETS", "CARD_02", "CARD_03", "CARD_04", "CARD_05", "CARD_06_NETS", "CARD_07_NETS"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_TERMINAL_ONLY_FI,
                        Set.of("VerifyCardArticlesTapToPayFlow", "VerifyOverviewPumaElementsFlow")
                )
        ));
    }
}