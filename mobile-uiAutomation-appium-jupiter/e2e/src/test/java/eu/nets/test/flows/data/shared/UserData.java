package eu.nets.test.flows.data.shared;

import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.flows.data.models.MpaCard;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.PropertiesUtil;
import org.openqa.selenium.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserData {
    public static List<MpaUser> allUsers() {
        return new ArrayList<>(List.of(
                new MpaUser(
                        "SMP",
                        "standard, SmartPay Blue Portal, 1 outlet",
                        null,
                        null,
                        null,
                        null,
                        "Germany",
                        PropertiesUtil.MPA.getProperty("mail.ads.smp12pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP,
                        Set.of("RegistrationFlow", "VerifyCardArticlesSMPFlow", "VerifyTerminalSupportSMPGermanFlow", "LanguageSwitchFlow", "VerifyTapToPayElementsFlow", "VerifyPortalAdvertFlow", "PinSetupFlow", "VerifyEmptyState")
                ),
                new MpaUser(
                        "SMP",
                        "standard, SmartPay Blue Portal, 1 outlet, Receipt_Tip_True",
                        null,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.smp23pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.23pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
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
                        "Switzerland",
                        PropertiesUtil.MPA.getProperty("mail.ads.ttpoa_ch_term.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_ch"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NEXI_licenceActivation", "CARD_07_NEXI_gettingStartedWithNexiSoftPOS"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_CH,
                        Set.of("RegistrationFlow", "VerifyTapToPayElementsFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_terminal, android_licence ready for activation",
                        null,
                        null,
                        null,
                        null,
                        "Germany",
                        PropertiesUtil.MPA.getProperty("mail.ads.12months26.gmail"),
                        null,
                        // This account will see Tap to Pay related cards with "Nexi SoftPOS" on Android
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NEXI_licenceActivation", "CARD_07_NEXI_gettingStartedWithNexiSoftPOS"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_ANDROID_LICENCE_12MONTHS26,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "TapToPay_Android_Nexi", "VerifyCardArticlesSMPFlow", "VerifyTapToPayElementsFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_terminal, android_licence ready for activation",
                        Platform.ANDROID,
                        null,
                        null,
                        null,
                        "Germany",
                        PropertiesUtil.MPA.getProperty("mail.ads.12months26.gmail"),
                        null,
                        // This account will see Tap to Pay related cards with "Nexi SoftPOS" on Android
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NEXI_licenceActivation", "CARD_07_NEXI_gettingStartedWithNexiSoftPOS"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_ANDROID_LICENCE_12MONTHS26,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "TapToPay_Android_Nexi", "VerifyCardArticlesSMPFlow", "VerifyTapToPayElementsFlow", "VerifyDeviceNotSupportedFlow")
                ),
                new MpaUser(
                        "SMP",
                        "1 < outlets < 20",
                        null,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.a12m.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_CH,
                        Set.of("VerifyCardArticlesSMPFlow")
                ),
                new MpaUser(
                        "SMP",
                        "one company",
                        null,
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        PropertiesUtil.MPA.getProperty("vat.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("ads.smp12pp"),
                        PropertiesUtil.MPA.getProperty("country.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("mail.ads.smp12pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp.12pp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
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
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.bau.gmail"),
                        PropertiesUtil.MPA.getProperty("org.bau"),
                        null,
                        AndroidSnapshot.MPA_LOGGED_IN_BAU,
                        Set.of("RegistrationFlow", "VerifyCardArticlesBAUFlow", "VerifyPortalAdvertFlow", "VerifyBAUStatementsFlow", "VerifyEmptyState")
                ),
                new MpaUser(
                        "BAU",
                        "standard, DACH BAU Portal (Orange Portal), 20+ outlets, BAU=MyCC",
                        Platform.ANDROID,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.bau.gmail"),
                        PropertiesUtil.MPA.getProperty("org.bau"),
                        includeCards("CARD_01_NEXI", "CARD_02_invoices_statement", "CARD_03_statementReport", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_BAU,
                        Set.of("RegistrationFlow", "VerifyCardArticlesBAUFlow")
                ),
                new MpaUser(
                        "BAU",
                        "standard, DACH BAU Portal (Orange Portal), 20+ outlets, BAU=MyCC",
                        Platform.IOS,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.bau.gmail"),
                        PropertiesUtil.MPA.getProperty("org.bau"),
                        includeCards("CARD_01_NETS", "CARD_02_invoices_statement", "CARD_03_statementReport", "CARD_05"),
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
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.tnp11.gmail"),
                        PropertiesUtil.MPA.getProperty("org.tnp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_TNP11,
                        Set.of("RegistrationFlow", "VerifyCardArticlesTNPFlow", "VerifyPortalAdvertFlow")
                ),
                new MpaUser(
                        "TNP",
                        "no phone number",
                        null,
                        PropertiesUtil.MPA.getProperty("fullName.tnp11"),
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.tnp11.gmail"),
                        PropertiesUtil.MPA.getProperty("org.tnp"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
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
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma23874pp.gmail"),
                        PropertiesUtil.MPA.getProperty("org.puma.standard"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_08"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA,
                        Set.of("RegistrationFlow", "VerifyCardArticlesPUMAFlow", "VerifyPortalAdvertFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "terminal_only",
                        null,
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
                        "acquiring_only, TTPOI_Nordics_Enabled",
                        null,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma950.gmail"),
                        PropertiesUtil.MPA.getProperty("org.puma.acquiringOnly"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
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
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.puma950.gmail"),
                        PropertiesUtil.MPA.getProperty("orgs.puma950"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
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
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.mepo.gmail"),
                        PropertiesUtil.MPA.getProperty("org.mepo"),
                        includeCards("CARD_02", "CARD_03_settlementReport", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_MEPO,
                        Set.of("RegistrationFlow", "VerifyCardArticlesMEPOFlow", "VerifyPortalAdvertFlow", "VerifyEmptyState")
                ),
                new MpaUser(
                        "CVR",
                        "single admin",
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("vat.cvr.singleAdmin"),
                        null,
                        PropertiesUtil.MPA.getProperty("country.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("email.cvr.singleAdmin"),
                        PropertiesUtil.MPA.getProperty("org.cvr.singleAdmin"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_CVR_SINGLE_ADMIN,
                        Set.of("RegistrationCvrFlow")
                ),
                new MpaUser(
                        "CVR",
                        "multiple admins",
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("vat.cvr.multipleAdmins"),
                        null,
                        PropertiesUtil.MPA.getProperty("country.cvr.multipleAdmins"),
                        PropertiesUtil.MPA.getProperty("email.cvr.multipleAdmins"),
                        PropertiesUtil.MPA.getProperty("org.cvr.multipleAdmins"),
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05"),
                        AndroidSnapshot.MPA_LOGGED_IN_CVR_MULTIPLE_ADMINS,
                        Set.of("RegistrationCvrFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_ttpoi, ios_licence ready for activation, no_physical_terminal",
                        null,
                        null,
                        null,
                        null,
                        "Germany",
                        PropertiesUtil.MPA.getProperty("mail.ads.ttpoi14.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_ttpoi14"),
                        //This account will see Tap to Pay related cards with "Nexi SoftPOS" on iOS
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_05", "CARD_06_NEXI", "CARD_07_NEXI"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_IOS_LICENCE_TTPOI14,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "VerifyCardArticlesTapToPayFlow", "VerifyTapToPayElementsFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_ttpoi, ios_licence ready for activation, no_physical_terminal",
                        Platform.IOS,
                        null,
                        null,
                        null,
                        "Germany",
                        PropertiesUtil.MPA.getProperty("mail.ads.ttpoi14.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_ttpoi14"),
                        //This account will see Tap to Pay related cards with "Nexi SoftPOS" on iOS
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_05", "CARD_06_NEXI", "CARD_07_NEXI"),
                        AndroidSnapshot.MPA_LOGGED_IN_SMP_IOS_LICENCE_TTPOI14,
                        Set.of("VerifyTerminalSupportSMPGermanFlow", "VerifyCardArticlesTapToPayFlow", "VerifyTapToPayElementsFlow", "VerifyDeviceNotSupportedFlow")
                ),
                new MpaUser(
                        "SMP",
                        "german_ttpoi, ios_licence, no_physical_terminal",
                        null,
                        null,
                        null,
                        null,
                        null,
                        PropertiesUtil.MPA.getProperty("mail.ads.ios1865p1.gmail"),
                        PropertiesUtil.MPA.getProperty("org.smp_1865p1"),
                        //This account will see Tap to Pay related cards with "Nexi SoftPOS" on iOS
                        includeCards("CARD_01_NEXI", "CARD_02", "CARD_03_settlementReport", "CARD_05", "CARD_06_NEXI", "CARD_07_NEXI"),
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
                        "Denmark",
                        PropertiesUtil.MPA.getProperty("mail.ads.puma_both"),
                        PropertiesUtil.MPA.getProperty("org.puma.both"),
                        // This account will see Tap to Pay related card with "Nets SoftPOS" on iOS
                        includeCards("CARD_01_NETS", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NETS", "CARD_07_NETS"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_BOTH,
                        Set.of("VerifyCardArticlesTapToPayFlow", "VerifyOverviewPumaElementsFlow", "VerifyTapToPayElementsFlow", "VerifyEmptyState")
                ),
                new MpaUser(
                        "PUMA",
                        "ios_nordics_ttpoi, puma_both",
                        Platform.IOS,
                        null,
                        null,
                        null,
                        "Denmark",
                        PropertiesUtil.MPA.getProperty("mail.ads.puma_both"),
                        PropertiesUtil.MPA.getProperty("org.puma.both"),
                        // This account will see Tap to Pay related card with "Nets SoftPOS" on iOS
                        includeCards("CARD_01_NETS", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NETS", "CARD_07_NETS"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_BOTH,
                        Set.of("VerifyCardArticlesTapToPayFlow", "VerifyOverviewPumaElementsFlow", "VerifyTapToPayElementsFlow", "VerifyDeviceNotSupportedFlow")
                ),
                new MpaUser(
                        "PUMA",
                        "ios_nordics_ttpoi, puma_terminal_only",
                        null,
                        null,
                        null,
                        null,
                        "Finland",
                        PropertiesUtil.MPA.getProperty("mail.ads.puma_terminal_only"),
                        PropertiesUtil.MPA.getProperty("org.puma.terminalOnly_FI"),
                        // This account will see Tap to Pay related card with "Nets SoftPOS" on iOS
                        includeCards("CARD_01_NETS", "CARD_02", "CARD_03_settlementReport", "CARD_04", "CARD_05", "CARD_06_NETS", "CARD_07_NETS"),
                        AndroidSnapshot.MPA_LOGGED_IN_PUMA_TERMINAL_ONLY_FI,
                        Set.of("VerifyCardArticlesTapToPayFlow", "VerifyOverviewPumaElementsFlow")
                )
        ));
    }

    private static List<MpaCard> includeCards(String... cardPrefixes) {
        return Arrays.stream(CardArticleData.class.getDeclaredFields())
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
        return Arrays.stream(CardArticleData.class.getDeclaredFields())
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
}
