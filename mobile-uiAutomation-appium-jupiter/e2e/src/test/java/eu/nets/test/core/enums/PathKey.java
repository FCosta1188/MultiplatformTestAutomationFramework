package eu.nets.test.core.enums;

import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.core.exceptions.UnupportedOsException;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;

public enum PathKey {
    ADB,
    EMULATOR,
    APPIUM_SERVER,
    MPA,
    DICTIONARIES,
    IO_APPIUM_SETTINGS,
    CHROME_DRIVER,
    IMG,
    ALLURE_RESULTS,
    CUSTOM_ALLURE_REPORTS,
    LOKALISE_BUNDLE;

    public static final String MPA_APK_FILENAME = "mpa.apk";
    public static final String MPA_APP_FILENAME = "mpa.app";
    public static final String IO_APPIUM_SETTINGS_APK_FILENAME = "io-appium-settings_apk-debug.apk";
    public static final String SEPARATOR = File.separator;
    public static final Path E2E_MODULE_ROOT = Paths.get(".." + SEPARATOR + "e2e");
    public static final String USER_HOME = System.getProperty("user.home");

    public static class PathHolder {
        private final String path;

        public PathHolder(String path) {
            this.path = path;
        }

        public String asString() {
            return path;
        }

        public Path asPath() {
            return Paths.get(path);
        }

        public File asFile() {
            return new File(path);
        }
    }

    public PathHolder resolve() {
        Path path;

        switch (this) {
            case ADB:
                path = EnvUtil.isMac()
                        ? Paths.get(USER_HOME, "Library/Android/sdk/platform-tools/adb")
                        : Paths.get("adb");
                break;
            case EMULATOR:
                path = EnvUtil.isMac()
                        ? Paths.get(USER_HOME, "Library/Android/sdk/emulator/emulator")
                        : Paths.get("emulator");
                break;
            case MPA:
                if (EnvUtil.isAndroid()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "android-apks", MPA_APK_FILENAME));
                } else if (EnvUtil.isIos()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "ios-apps", MPA_APP_FILENAME));
                } else {
                    throw new UnsupportedPlatformException();
                }
                break;

            case DICTIONARIES:
                path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "dictionaries"));
                break;

            case IO_APPIUM_SETTINGS:
                path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "android-apks", IO_APPIUM_SETTINGS_APK_FILENAME));
                break;

            case CHROME_DRIVER:
                if (EnvUtil.isWin32()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "chromedriver", "chromedriver-win32", "chromedriver"));
                } else if (EnvUtil.isWin64()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "chromedriver", "chromedriver-win64", "chromedriver"));
                } else if (EnvUtil.isMacIntel()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "chromedriver", "chromedriver-mac-x64", "chromedriver"));
                } else if (EnvUtil.isMacAppleSilicon()) {
                    path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "chromedriver", "chromedriver-mac-arm64", "chromedriver"));
                } else {
                    throw new UnupportedOsException();
                }
                break;

            case IMG:
                path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "img"));
                break;

            case ALLURE_RESULTS:
                path = E2E_MODULE_ROOT.resolve(Paths.get("build", "allure-results"));
                break;

            case CUSTOM_ALLURE_REPORTS:
                path = E2E_MODULE_ROOT.resolve(Paths.get("src", "test", "resources", "reports"));
                break;

            case LOKALISE_BUNDLE:
                path = E2E_MODULE_ROOT.resolve(Paths.get(
                        PropertiesUtil.CONFIG.getProperty("lokalise.bundle.dir").replace("|", SEPARATOR))
                );
                break;

            case APPIUM_SERVER:
                path = getAppiumMainjsFilepath();
                break;

            default:
                throw new IllegalArgumentException(logError("Invalid path key: " + this));
        }

        if (path.toString().isBlank()) {
            throw new RuntimeException(logError("Unable to build path: OS = " + EnvUtil.OS + ", key = " + this));
        }

        return new PathHolder(path.toString());
    }

    private Path getAppiumMainjsFilepath() {
        List<String> candidatePaths = new ArrayList<>();
        String fallbackPathString;

        if (EnvUtil.isWin()) {
            candidatePaths.add(
                    String.join(SEPARATOR, USER_HOME, "AppData", "Roaming", "npm", "node_modules", "appium", "build", "lib", "main.js")
            );

            fallbackPathString = PropertiesUtil.CONFIG.getProperty("appium.mainjs.win");
        } else if (EnvUtil.isMac()) {
            // Common MacOS paths: Apple Silicon, Intel, NVM, Yarn
            candidatePaths.add("/opt/homebrew/lib/node_modules/appium/build/lib/main.js");
            candidatePaths.add("/usr/local/lib/node_modules/appium/build/lib/main.js");
            candidatePaths.add(USER_HOME + "/.nvm/versions/node/v18.16.0/lib/node_modules/appium/build/lib/main.js");
            candidatePaths.add(USER_HOME + "/.yarn/global/node_modules/appium/build/lib/main.js");

            fallbackPathString = PropertiesUtil.CONFIG.getProperty("appium.mainjs.mac");
        } else {
            throw new UnupportedOsException();
        }

        //Add path from npm root to candidates
        try {
            ProcessBuilder npmRootProcessBuilder = new ProcessBuilder("npm", "root", "-g");
            Process npmRootProcess = npmRootProcessBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(npmRootProcess.getInputStream()))) {
                String npmRoot = reader.readLine();
                if (npmRoot != null && !npmRoot.trim().isEmpty()) {
                    candidatePaths.add(npmRoot.trim() + "/appium/build/lib/main.js");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(logError("Unable to get 'npm root -g': " + e.getMessage()));
        }

        // 1. Try each candidate
        for (String pathString : candidatePaths) {
            Path path = Paths.get(pathString);
            if (Files.exists(path)) {
                logInfo("Appium main.js found at: " + path);
                return path;
            }
        }

        // 2. If no candidate is valid, try fallback path from properties
        if (!fallbackPathString.isEmpty()) {
            Path fallbackPath = Paths.get(fallbackPathString);
            if (Files.exists(fallbackPath)) {
                logInfo("Appium main.js found at fallback path: " + fallbackPath);
                return fallbackPath;
            }
        }

        // 3. If fallback fails, throw exception
        throw new IllegalStateException(logError(
                "Appium main.js not found. Please ensure Appium is installed globally with `npm install -g appium`\n" +
                        "Candidate paths:\n\t- " + String.join("\n\t- ", candidatePaths) + "\n" +
                        "Fallback path:\n - " + fallbackPathString + "\n"
        ));
    }
}
