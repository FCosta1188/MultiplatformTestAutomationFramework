package eu.nets.test.flows.VerifyCardArticles;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.MpaWidget;
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

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyCardArticlesSMPFlow extends AbstractFlow {
    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return AndroidSnapshot.MPA_LOGGED_IN_SMP_12PP;
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}, {2}")
    @MethodSource("eu.nets.test.flows.data.VerifyCardArticles.VerifyCardArticlesData#streamSMP")
    @Epic("Verify cards and related articles")
    @Feature(
            """
                    https://nexigroup-germany.atlassian.net/browse/MSA-6208
                    https://nexigroup-germany.atlassian.net/browse/MSA-6210
                    https://nexigroup-germany.atlassian.net/browse/MSA-6283
                    https://nexigroup-germany.atlassian.net/browse/MSA-6285
                    https://nexigroup-germany.atlassian.net/browse/MSA-6303
                    https://nexigroup-germany.atlassian.net/browse/MSA-6304
                    https://nexigroup-germany.atlassian.net/browse/MSA-6307
                    https://nexigroup-germany.atlassian.net/browse/MSA-6308
                    https://nexigroup-germany.atlassian.net/browse/MSA-6311
                    https://nexigroup-germany.atlassian.net/browse/MSA-6313
                    https://nexigroup-germany.atlassian.net/browse/MSA-6314
                    https://nexigroup-germany.atlassian.net/browse/MSA-6317
                    https://nexigroup-germany.atlassian.net/browse/MSA-6318
                    https://nexigroup-germany.atlassian.net/browse/MSA-6321
                    https://nexigroup-germany.atlassian.net/browse/MSA-6322
                    """
    )
    @Story("Verify cards and related articles for SMP users")
    @Description(
            """
                        Applicable cards for specific SMP users are available in the required app sections.
                        Cards and related articles match UI requirements (text, images, colors). Articles can be opened, scrolled and closed.
                        Cards disappear from the section when tapping their X button.
                        Cards reappear in the OVERVIEW section when loggingOut->loggingIn or uninstalling->reinstalling.
                    """
    )
    protected void runTest(
            MpaUser user,
            MpaWidget[] appSections,
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        run(user, appSections, appLanguage);
    }

    protected void run(
            MpaUser user,
            MpaWidget[] appSections,
            MpaLanguage appLanguage
    ) throws IOException, InterruptedException {
        new VerifyCardArticlesBaseFlow().run(user, appSections, appLanguage);
    }
}
