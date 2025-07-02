package eu.nets.test.flows.VerifyCardArticles;

import eu.nets.test.core.AbstractFlow;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.MpaWidget;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.flows.data.models.MpaUser;
import eu.nets.test.util.EnvUtil;
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

import static eu.nets.test.util.AllureUtil.logError;

@ExtendWith(AllureJunit5.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VerifyCardArticlesTapToPayFlow extends AbstractFlow {
    @Override
    public AndroidSnapshot startupAndroidSnapshot() {
        return null;
    }

    @Override
    public String flowClassName() {
        return "VerifyCardArticlesTapToPayFlow";
    }

    @ParameterizedTest(name = "[{index}] {0}, {1}, {2}")
    @MethodSource("eu.nets.test.flows.data.VerifyCardArticles.VerifyCardArticlesData#streamTapToPay")
    @Epic("Verify cards and related articles")
    @Feature(
            """
                    https://nexigroup-germany.atlassian.net/browse/MSA-6315
                    https://nexigroup-germany.atlassian.net/browse/MSA-6316
                    https://nexigroup-germany.atlassian.net/browse/MSA-6319
                    https://nexigroup-germany.atlassian.net/browse/MSA-6320
                    """
    )
    @Story("Verify cards and related articles for TapToPay users")
    @Description()
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
        if (EnvUtil.isIos()) {
            new VerifyCardArticlesBaseFlow().run(user, appSections, appLanguage);
        } else if (EnvUtil.isAndroid()) {
            throw new RuntimeException(logError(
                    "TapToPay feature not supported on Android yet. Please run the test on iOS only (set PLATFORM=ios in .env file)."));
        } else {
            throw new UnsupportedPlatformException();
        }
    }
}
