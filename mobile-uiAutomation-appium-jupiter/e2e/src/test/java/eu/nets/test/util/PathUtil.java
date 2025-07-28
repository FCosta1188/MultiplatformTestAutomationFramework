package eu.nets.test.util;

import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.core.exceptions.UnupportedOsException;

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

@Deprecated
public final class PathUtil {
    public static final String MPA_APK_FILENAME = "mpa.apk";
    public static final String MPA_APP_FILENAME = "mpa.app";
    public static final String IO_APPIUM_SETTINGS_APK_FILENAME = "io-appium-settings_apk-debug.apk";

    public static final String SEPARATOR = File.separator;
    public static final Path E2E_MODULE_PATH = Paths.get(".." + SEPARATOR + "e2e");
    public static final String USER_HOME = System.getProperty("user.home");

    public static class PathHolder {
        private final String path;

        private PathHolder(String path) {
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

    public static PathHolder get(String pathName) {
        Path baseDir;
        if (EnvUtil.isWin() || EnvUtil.isMac()) {
            baseDir = E2E_MODULE_PATH;
        } else {
            throw new UnupportedOsException();
        }

        Path path;
        switch (pathName) {
            case "appiumServer":
                path = getAppiumMainjsFilepath();
                break;
            case "adb": //paths to adb executable only, no folders
                if (EnvUtil.isMac()) {
                    path = Paths.get(System.getProperty("user.home"), "Library/Android/sdk/platform-tools/adb");
                } else if (EnvUtil.isWin()) {
                    path = Paths.get("adb"); //if adb is in PATH, just use "adb"
                } else {
                    throw new UnupportedOsException();
                }
                break;
            case "emulator": //paths to android emulator executable only, no folders
                if (EnvUtil.isMac()) {
                    path = Paths.get(System.getProperty("user.home"), "Library/Android/sdk/emulator/emulator");
                } else if (EnvUtil.isWin()) {
                    path = Paths.get("emulator"); //if emulator is in PATH, just use "emulator"
                } else {
                    throw new UnupportedOsException();
                }
                break;
            case "mpa":
                if (EnvUtil.isAndroid()) {
                    path = baseDir.resolve(Paths.get("src", "test", "resources", "android-apks", MPA_APK_FILENAME));
                } else if (EnvUtil.isIos()) {
                    path = baseDir.resolve(Paths.get("src", "test", "resources", "ios-apps", MPA_APP_FILENAME));
                } else {
                    throw new UnsupportedPlatformException();
                }
                break;
            case "ioAppiumSettings":
                path = baseDir.resolve(Paths.get("src", "test", "resources", "android-apks", IO_APPIUM_SETTINGS_APK_FILENAME));
                break;
            case "img":
                path = baseDir.resolve(Paths.get("src", "test", "resources", "img"));
                break;
            case "projectHome":
                path = baseDir;
                break;
            case "allureResults":
                path = baseDir.resolve(Paths.get("build", "allure-results"));
                break;
            case "customAllureReports":
                path = baseDir.resolve(Paths.get("src", "test", "resources", "reports"));
                break;
            case "lokaliseBundle":
                path = baseDir.resolve(Paths.get(PropertiesUtil.CONFIG.getProperty("lokalise.bundle.dir").replace("|", SEPARATOR)));
                break;

            default:
                throw new IllegalArgumentException(logError("Invalid pathName: " + pathName));
        }

        if (path == null || path.toString().isEmpty() || path.toString().isBlank()) {
            throw new RuntimeException(logError("Unable to build path: OS = " + EnvUtil.OS + ", pathName = " + pathName));
        }

        return new PathHolder(path.toString());
    }

    public static Path getAppiumMainjsFilepath() {
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
