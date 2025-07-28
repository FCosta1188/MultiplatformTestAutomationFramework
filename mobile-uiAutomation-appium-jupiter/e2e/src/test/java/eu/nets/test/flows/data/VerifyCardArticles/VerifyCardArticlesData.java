package eu.nets.test.flows.data.VerifyCardArticles;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.MpaWidget;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.data.shared.UserData;
import eu.nets.test.util.EnvUtil;
import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.Platform;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static eu.nets.test.core.enums.MpaWidget.OVERVIEW_DASHBOARD_BUTTON;
import static eu.nets.test.core.enums.MpaWidget.SUPPORT_DASHBOARD_BUTTON;

public final class VerifyCardArticlesData {
    public static Stream<Arguments> streamSMP() {
        List<Arguments> smpUsers = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyCardArticlesSMPFlow"))
                .map(Arguments::of)
                .toList();

        return smpUsers.stream()
                .flatMap(smpUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        smpUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamPUMA() {
        List<Arguments> pumaUsers = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyCardArticlesPUMAFlow"))
                .map(Arguments::of)
                .toList();

        return pumaUsers.stream()
                .flatMap(pumaUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        pumaUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamTNP() {
        List<Arguments> tnpUsers = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyCardArticlesTNPFlow"))
                .map(Arguments::of)
                .toList();

        return tnpUsers.stream()
                .flatMap(tnpUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        tnpUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamBAU() {
        List<Arguments> bauUsers;
        if(EnvUtil.isAndroid()) {
            bauUsers = UserData.allUsers().stream()
                    .filter(user ->
                            user.platform() != null &&
                            user.platform().equals(Platform.ANDROID) &&
                            user.flowTestTags().contains("VerifyCardArticlesBAUFlow"))
                    .map(Arguments::of)
                    .toList();
        } else if (EnvUtil.isIos()) {
            bauUsers = UserData.allUsers().stream()
                    .filter(user ->
                            user.platform() != null &&
                            user.platform().equals(Platform.IOS) &&
                            user.flowTestTags().contains("VerifyCardArticlesBAUFlow"))
                    .map(Arguments::of)
                    .toList();
        } else {
            throw new UnsupportedPlatformException();
        }

        return bauUsers.stream()
                .flatMap(bauUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        bauUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamMEPO() {
        List<Arguments> mepoUsers = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyCardArticlesMEPOFlow"))
                .map(Arguments::of)
                .toList();

        return mepoUsers.stream()
                .flatMap(mepoUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        mepoUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }

    public static Stream<Arguments> streamTapToPay() {
        List<Arguments> tapToPayUsers = UserData.allUsers().stream()
                .filter(user -> user.flowTestTags().contains("VerifyCardArticlesTapToPayFlow"))
                .map(Arguments::of)
                .toList();

        return tapToPayUsers.stream()
                .flatMap(tapToPayUser ->
                        Arrays.stream(MpaLanguage.values())
                                .filter(lang -> lang.isSupportedByMpa())
                                .map(lang -> Arguments.of(
                                        tapToPayUser.get()[0], // user
                                        new MpaWidget[]{
                                                OVERVIEW_DASHBOARD_BUTTON,
                                                SUPPORT_DASHBOARD_BUTTON
                                        }, // appSections
                                        lang // appLanguage
                                ))
                );
    }
}
