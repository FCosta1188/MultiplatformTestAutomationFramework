package eu.nets.test.util;

import eu.nets.test.core.enums.AndroidSnapshot;
import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.PathKey;
import eu.nets.test.core.exceptions.UnupportedOsException;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.nets.test.util.AllureUtil.logError;

public final class EnvUtil {

    public static final String OS = System.getProperty("os.name");
    public static final String OS_ARCH = System.getProperty("os.arch");
    public static final String ADB = PathKey.ADB.resolve().asString();
    public static final String EMULATOR = PathKey.EMULATOR.resolve().asString();

    private static final int MAX_TRIES = 3;
    private static final int POLL_INTERVAL_MS = 1000;

    public static boolean isWin() {
        return OS.toLowerCase().contains("windows");
    }

    public static boolean isMacOs() {
        return OS.toLowerCase().contains("mac os x");
    }

    public static boolean isAppleSilicon() {
        return OS_ARCH.equalsIgnoreCase("aarch64");
    }

    public static boolean isAndroid() {
        return PropertiesUtil.ENV.getProperty("PLATFORM").equalsIgnoreCase("android");
    }

    public static boolean isIos() {
        return PropertiesUtil.ENV.getProperty("PLATFORM").equalsIgnoreCase("ios");
    }

    private static List<String> getAndroidEmulatorCommand(AndroidSnapshot androidSnapshot) {
        List<String> args = new ArrayList<>();

        args.add(EMULATOR);
        args.add("-avd");
        args.add(PropertiesUtil.ENV.getProperty("emulatorDeviceName"));
        args.add("-no-boot-anim");
        args.add("-noaudio");

        if (androidSnapshot != null) {
            args.add("-snapshot");
            args.add(androidSnapshot.getName());
        } else {
            args.add("-no-snapshot");
        }

        if (isWin()) {
            args.add("-gpu");
            args.add("on");
        } else if (isMacOs()) {
            if (isAppleSilicon()) { //ARM Mac
                args.add("-gpu");
                args.add("swiftshader_indirect"); // Compatibile with M1/M2/M3
                args.add("-accel");
                args.add("on");
            } else { // Intel Mac
                args.add("-gpu");
                args.add("host");
            }
        } else {
            throw new UnupportedOsException();
        }

        return args;
    }

    private static Map<String, String> getMacOsEnv() {
        Map<String, String> env = new HashMap<>(System.getenv());

        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome == null || androidHome.isBlank()) {
            androidHome = System.getProperty("user.home") + "/Library/Android/sdk";
            env.put("ANDROID_HOME", androidHome);
        }

        String androidSdkRoot = System.getenv("ANDROID_SDK_ROOT");
        if (androidSdkRoot == null || androidSdkRoot.isBlank()) {
            androidSdkRoot = System.getProperty("user.home") + "/Library/Android/sdk";
            env.put("ANDROID_SDK_ROOT", androidSdkRoot);
        }

        String existingPath = env.getOrDefault("PATH", "");
        String[] additionalPaths = {
                "/opt/homebrew/bin",
                androidHome + "/emulator", // Android emulator
                androidHome + "/platform-tools", // Android SDK platform-tools
                androidHome + "/tools", // Android SDK tools
                "/Applications/Xcode.app/Contents/Developer/usr/bin" // Xcode command line tools
        };

        StringBuilder updatedPath = new StringBuilder(existingPath);
        for (String path : additionalPaths) {
            if (!existingPath.contains(path)) {
                updatedPath.append(":").append(path);
            }
        }
        env.put("PATH", updatedPath.toString());

        return env;
    }

    private static void setProcessBuilderEnv(ProcessBuilder processBuilder) {
        if (isMacOs()) {
            processBuilder.environment().putAll(getMacOsEnv());
        }
    }

    private static void startAdbServer() throws IOException, InterruptedException {
        boolean isAdbServerRunning = false;

        ProcessBuilder listAdbDevicesBuilder = new ProcessBuilder(ADB, "devices");
        setProcessBuilderEnv(listAdbDevicesBuilder);
        Process listAdbDevices = listAdbDevicesBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(listAdbDevices.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("List of devices")) {
                isAdbServerRunning = true;
                break;
            }
        }

        if (!isAdbServerRunning) {
            ProcessBuilder startAdbServerBuilder = new ProcessBuilder(ADB, "start-server");
            setProcessBuilderEnv(startAdbServerBuilder);
            startAdbServerBuilder.start().waitFor();
        }

        // isDeviceOnline
        for (int i = 0; i < MAX_TRIES; i++) {
            listAdbDevices = listAdbDevicesBuilder.start();
            reader = new BufferedReader(new InputStreamReader(listAdbDevices.getInputStream()));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("emulator-5554") && line.contains("device")) {
                    break;
                }
            }

            EnvUtil.safeSleep(POLL_INTERVAL_MS);
        }
    }

    private static void disableAndroidEmulatorAnimations() throws IOException, InterruptedException {
        List<ProcessBuilder> builders = List.of(
                new ProcessBuilder(ADB, "shell", "settings", "put", "global", "window_animation_scale", "0"),
                new ProcessBuilder(ADB, "shell", "settings", "put", "global", "transition_animation_scale", "0"),
                new ProcessBuilder(ADB, "shell", "settings", "put", "global", "animator_duration_scale", "0")
        );

        for (ProcessBuilder builder : builders) {
            setProcessBuilderEnv(builder);
            builder.start().waitFor();
        }
    }

    public static void startupAndroidEmulator() throws IOException, InterruptedException {
        startAdbServer();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);

        List<String> args = new ArrayList<>(getAndroidEmulatorCommand(null));
        ProcessBuilder launchAdv = new ProcessBuilder(args);

        setProcessBuilderEnv(launchAdv);
        launchAdv.start();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);
        disableAndroidEmulatorAnimations();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);
    }

    public static void startupAndroidEmulator(AndroidSnapshot androidSnapshot) throws IOException, InterruptedException {
        startAdbServer();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);

        List<String> args = new ArrayList<>(getAndroidEmulatorCommand(androidSnapshot));
        ProcessBuilder launchAdv = new ProcessBuilder(args);

        setProcessBuilderEnv(launchAdv);
        launchAdv.start();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);
        disableAndroidEmulatorAnimations();
        EnvUtil.safeSleep(POLL_INTERVAL_MS);
    }

    public static void shutdownAndroidEmulator() throws IOException {
        ProcessBuilder killAdbEmu = new ProcessBuilder(ADB, "emu", "kill");
        ProcessBuilder killAdbServer = new ProcessBuilder(ADB, "kill-server");
        setProcessBuilderEnv(killAdbEmu);
        setProcessBuilderEnv(killAdbServer);
        killAdbEmu.start();
        killAdbServer.start();
    }

    public static void startupIosSimulator() throws IOException {
        if (!isMacOs()) {
            throw new UnsupportedOperationException("iOS Simulator is only supported on MacOS.");
        }

        String udid = PropertiesUtil.ENV.getProperty("simulatorDeviceUdid");
        if (udid == null || udid.isBlank()) {
            throw new IllegalArgumentException("Simulator with udid '" + udid + "' not found.");
        }

        new ProcessBuilder("xcrun", "simctl", "boot", udid).start();
        new ProcessBuilder("open", "-a", "Simulator").start(); // Optional: open simulator app (GUI)
    }

    public static void shutdownIosSimulator() throws IOException {
        if (!isMacOs()) {
            throw new UnsupportedOperationException("iOS Simulator is only supported on MacOS.");
        }

        ProcessBuilder shutdownAllSimulators = new ProcessBuilder("xcrun", "simctl", "shutdown", "all");
        ProcessBuilder killSimulatorApp = new ProcessBuilder("killall", "Simulator");
        shutdownAllSimulators.start();
        killSimulatorApp.start();
    }

    private static AppiumServiceBuilder getAppiumServiceBuilder() {
        if (isMacOs()) {
            return new AppiumServiceBuilder()
                    .withAppiumJS(PathKey.APPIUM_SERVER.resolve().asFile())
                    .usingPort(Integer.parseInt(PropertiesUtil.CONFIG.getProperty("appium.server.port")))
                    .withEnvironment(getMacOsEnv());
        } else if (isWin()) {
            return new AppiumServiceBuilder()
                    .withAppiumJS(PathKey.APPIUM_SERVER.resolve().asFile())
                    .usingPort(Integer.parseInt(PropertiesUtil.CONFIG.getProperty("appium.server.port")));
        } else {
            throw new UnupportedOsException();
        }
    }

    public static AppiumDriverLocalService getAppiumServer() {
        return getAppiumServiceBuilder().build();
    }

    public static AppiumDriverLocalService getAppiumServer(String args) {
        AppiumServiceBuilder appiumServiceBuilder = getAppiumServiceBuilder();

        for (String arg : args.split("\\s+")) {
            appiumServiceBuilder.withArgument(() -> arg);
        }

        return appiumServiceBuilder.build();
    }

    public static DesiredCapabilities getDesideredCapabilities(int waitS, boolean setAppiumApp) {
        DesiredCapabilities caps = new DesiredCapabilities();

        if (setAppiumApp) {
            String apkPath = PathKey.MPA.resolve().asString();
            caps.setCapability("appium:app", apkPath);
        }
        if (isIos()) {
            caps.setCapability("platformVersion", PropertiesUtil.ENV.getProperty("platformVersion"));
            caps.setCapability("appium:deviceName", PropertiesUtil.ENV.getProperty("simulatorDeviceName"));
            caps.setCapability("useNewWDA", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.useNewWDA"));
            caps.setCapability("wdaLocalPort", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.wdaLocalPort"));
            caps.setCapability("wdaLaunchTimeout", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.wdaLaunchTimeout"));
            caps.setCapability("wdaConnectionTimeout", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.wdaConnectionTimeout"));
        }
        if (isAndroid()) {
            caps.setCapability("appium:deviceName", PropertiesUtil.ENV.getProperty("emulatorDeviceName"));
            caps.setCapability("uiautomator2ServerLaunchTimeout", 1000 * waitS);
            caps.setCapability("uiautomator2ServerInstallTimeout", 1000 * waitS);
            caps.setCapability("adbExecTimeout", 1000 * waitS);
            caps.setCapability("avdReadyTimeout", 1000 * waitS);
            caps.setCapability("appium:avdLaunchTimeout", 1000 * waitS);
        }
        caps.setCapability("platformName", PropertiesUtil.ENV.getProperty("PLATFORM"));
        caps.setCapability("appium:automationName", PropertiesUtil.ENV.getProperty("automationName"));
        caps.setCapability("newCommandTimeout", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.newCommandTimeout"));
        caps.setCapability("autoGrantPermissions", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.autoGrantPermissions"));
        caps.setCapability("appWaitForLaunch", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.appWaitForLaunch"));
        caps.setCapability("appWaitDuration", 1000 * waitS);
        caps.setCapability("disableWindowAnimation", PropertiesUtil.CONFIG.getProperty("appium.driver.caps.disableWindowAnimation"));
        caps.setCapability("language", MpaLanguage.DEFAULT.getLanguage());
        caps.setCapability("locale", MpaLanguage.DEFAULT.getCountry());

        return caps;
    }

    public static DesiredCapabilities getDesideredCapabilities(int waitS, boolean setAppiumApp, String language, String locale) {
        DesiredCapabilities caps = getDesideredCapabilities(waitS, setAppiumApp);
        caps.setCapability("language", language);
        caps.setCapability("locale", locale);

        return caps;
    }

    public static XCUITestOptions getXCUITestOptions() {
        XCUITestOptions options = new XCUITestOptions();
        options.setPlatformName(PropertiesUtil.ENV.getProperty("PLATFORM"))
                .setDeviceName(PropertiesUtil.ENV.getProperty("simulatorDeviceName"))
                .setAutomationName(PropertiesUtil.ENV.getProperty("automationName"))
                .setUdid(PropertiesUtil.ENV.getProperty("udid"))
                .setApp(System.getProperty("user.dir") + PropertiesUtil.ENV.getProperty("appPath"))
                .setUsePrebuiltWda(true)
                .autoAcceptAlerts()
                .setCapability("noReset", false);

        return options;
    }

    public static void emptyFolder(Path path, boolean deleteSubfolders) throws IOException {
        if (Files.exists(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry) && deleteSubfolders) {
                        emptyFolder(entry, true);
                    }
                    Files.delete(entry);
                }
            }
        }
    }

    public static void safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(logError("Thread.sleep interrupted: " + e.getMessage()));
        }
    }
}

