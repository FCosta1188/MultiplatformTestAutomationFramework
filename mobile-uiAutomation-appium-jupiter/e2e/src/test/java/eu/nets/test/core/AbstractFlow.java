package eu.nets.test.core;

import eu.nets.test.core.drivers.MpaAndroidDriver;
import eu.nets.test.core.drivers.MpaDriver;
import eu.nets.test.core.drivers.MpaIosDriver;
import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.util.AllureUtil;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

import static eu.nets.test.util.AllureUtil.emptyAllureResults;
import static eu.nets.test.util.AllureUtil.generateCustomReport;
import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;
import static eu.nets.test.util.EnvUtil.getAppiumServer;
import static eu.nets.test.util.EnvUtil.shutdownAndroidEmulator;
import static eu.nets.test.util.EnvUtil.shutdownIosSimulator;
import static eu.nets.test.util.EnvUtil.startupAndroidEmulator;
import static eu.nets.test.util.EnvUtil.startupIosSimulator;

/**
 * public abstract class AbstractFlow
 * <p>
 * Classe di base per tutti i test MPA.
 * Effettua setup e teardown comuni a tutti i test.
 * </p>
 *
 * <p>
 * * Utilizzo delle annotazioni JUnit 5:
 * *   @TestInstance(TestInstance.Lifecycle.PER_CLASS): annotare le sottoclassi di AbstractFlow per garantire l'esecuzione di @BeforeAll e @AfterAll, dato
 * che sono dichiarati non statici.
 * *   @ExtendWith(AllureJunit5.class): annotare tutte le sottoclassi di AbstractFlow che vogliamo generino un nuovo report (comprende i dati di tulle le run
 * contenute in build\allure-results)
 * * </p>
 *
 * @author Francesco Costa
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractFlow {

    private static final Logger log = LoggerFactory.getLogger(AbstractFlow.class);
    protected AppiumDriverLocalService service = getAppiumServer(PropertiesUtil.CONFIG.getProperty("appium.server.args"));
    protected MpaDriver driver;
    protected Map<MpaLanguage, Map<String, String>> lokaliseBundle;

    protected static Path reportDir;

    protected final String APP_PIN = PropertiesUtil.MPA.getProperty("app.pin");
    protected final String GMAIL_APP_PSW = PropertiesUtil.MPA.getProperty("mail.appPsw.main.gmail");

    @RegisterExtension
    protected final TestCallbackExtension testCallbackExtension = new TestCallbackExtension(
            () -> driver,
            () -> reportDir
    );

    /**
     * Nome dello snapshot da utilizzare per avviare l'emulatore.
     * Questo snapshot corrisponde alla configurazione dell'app "MyPayments" necessaria per eseguire il test in oggetto.
     *
     * @return il nome dello snapshot dell'emulatore
     */
    public abstract AndroidSnapshot startupAndroidSnapshot();

    public abstract String flowClassName();

    public MpaDriver getDriver() {
        return driver;
    }

    public void setDriver(MpaDriver driver) {
        this.driver = driver;
    }

    protected void launchDriver() throws MalformedURLException {
        if (this.driver == null) {
            if (EnvUtil.isAndroid()) {
                this.driver = new MpaAndroidDriver(200, false);
            } else if (EnvUtil.isIos()) {
                this.driver = new MpaIosDriver(200, false);
            } else {
                throw new UnsupportedPlatformException();
            }
        }
    }

    protected void launchDriver(boolean setAppiumApp) throws MalformedURLException {
        if (this.driver == null) {
            if (EnvUtil.isAndroid()) {
                this.driver = new MpaAndroidDriver(200, setAppiumApp);
            } else if (EnvUtil.isIos()) {
                this.driver = new MpaIosDriver(200, setAppiumApp);
            } else {
                throw new UnsupportedPlatformException();
            }
        }
    }

    protected void launchDriver(boolean setAppiumApp, String language, String locale) throws MalformedURLException {
        if (this.driver == null) {
            if (EnvUtil.isAndroid()) {
                this.driver = new MpaAndroidDriver(200, setAppiumApp, language, locale);
            } else if (EnvUtil.isIos()) {
                int retries = 0;
                while (driver == null && retries++ <= 3) {
                    try {
                        this.driver = new MpaIosDriver(200, setAppiumApp, language, locale);
                    } catch (Exception e) {
                        logError("Error launching iOS driver - retry #" + retries + ": " + e.getMessage());
                        EnvUtil.safeSleep(3000);
                    }
                }
            } else {
                throw new UnsupportedPlatformException();
            }
        }

        if (driver == null) {
            throw new IllegalStateException(logError("Unable to launch " + PropertiesUtil.ENV.getProperty("PLATFORM") + " driver."));
        }
    }

    @BeforeAll
    protected void beforeAll() throws IOException, InterruptedException {
        if (EnvUtil.isAndroid()) {
            if (startupAndroidSnapshot() != null) {
                startupAndroidEmulator(startupAndroidSnapshot());
            } else {
                EnvUtil.startupAndroidEmulator();
                logInfo("startupSnapshot() is null -> Emulator started without snapshot.");
            }
        } else if (EnvUtil.isIos()) {
            startupIosSimulator();
        } else {
            throw new UnsupportedPlatformException();
        }

        reportDir = AllureUtil.createCustomReportFolder(flowClassName());

        //        try{
        //            LokaliseUtil.downloadBundle(true);
        //            logInfo("Lokalise bundle downloaded with async API");
        //        } catch (Exception e) {
        //            logInfo("Lokalise bundle async API download failed, trying sync API...");
        //            LokaliseUtil.downloadBundle(false);
        //            logInfo("Lokalise bundle downloaded with sync API");
        //        }
        //        this.lokaliseBundle = LokaliseUtil.getBundle();

        Allure.parameter("Platform", PropertiesUtil.ENV.getProperty("PLATFORM"));

        if (!this.service.isRunning()) {
            this.service.start();
        }
    }

    @BeforeEach
    protected void beforeEach() {
        EnvUtil.safeSleep(5000);
    }

    @AfterEach
    protected void afterEach() {
        Allure.step("Quit driver", () -> {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    logError("Error while quitting driver: " + e.getMessage());
                }
                driver = null;
            }
        });

        EnvUtil.safeSleep(5000);
    }

    @AfterAll
    protected void afterAll() throws IOException, InterruptedException {
        if (this.driver != null) {
            this.driver.quit();
            this.driver = null;
        }

        if (this.service.isRunning()) {
            this.service.stop();
        }

        if (EnvUtil.isAndroid()) {
            shutdownAndroidEmulator();
        } else if (EnvUtil.isIos()) {
            shutdownIosSimulator();
        } else {
            throw new UnsupportedPlatformException();
        }

        generateCustomReport(reportDir);
        emptyAllureResults();
    }
}
