package eu.nets.test.core.enums;

import eu.nets.test.core.drivers.MpaAndroidDriver;
import eu.nets.test.util.EnvUtil;
import eu.nets.test.util.PropertiesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static eu.nets.test.util.AllureUtil.logError;

public enum AndroidSnapshot {
    //Installed apps: ioAppiumSettings (Android only)
    NO_MPA("ioAppiumSettings", "n/a"),

    //Installed apps: ioAppiumSettings, MPA (apk: no dexguard, setup: Permit screenshots)
    MPA_LOGGED_OUT("MPA-loggedOut", "n/a"),

    //Installed apps: ioAppiumSettings, MPA (apk: no dexguard, setup: Permit screenshots)
    //    SMP
    MPA_LOGGED_IN_SMP_12PP("MPA-loggedIn-SMP_12PP", PropertiesUtil.MPA.getProperty("mail.ads.smp12pp.gmail")),
    MPA_LOGGED_IN_SMP_23PP("MPA-loggedIn-SMP_23PP", PropertiesUtil.MPA.getProperty("mail.ads.smp23pp.gmail")),
    MPA_LOGGED_IN_SMP_CH("MPA-loggedIn-SMP-ch", PropertiesUtil.MPA.getProperty("mail.ads.ttpoa_ch_term.gmail")),
    MPA_LOGGED_IN_SMP_ANDROID_LICENCE("MPA-loggedIn-androidLicence", PropertiesUtil.MPA.getProperty("mail.ads.12months26.gmail")),
    MPA_LOGGED_IN_SMP_IOS_LICENCE_TTPOI14("MPA-loggedIn-ttpoi14", PropertiesUtil.MPA.getProperty("mail.ads.ttpoi14.gmail")),
    MPA_LOGGED_IN_SMP_IOS_LICENCE_1865P1("MPA-loggedIn-ios1865p1", PropertiesUtil.MPA.getProperty("mail.ads.ios1865p1.gmail")),
    //    PUMA
    MPA_LOGGED_IN_PUMA("MPA-loggedIn-PUMA", PropertiesUtil.MPA.getProperty("mail.ads.puma23874pp.gmail")),
    MPA_LOGGED_IN_PUMA_TERMINAL_ONLY("MPA-loggedIn-PUMA-terminalOnly", PropertiesUtil.MPA.getProperty("mail.ads.puma.yahoo")),
    MPA_LOGGED_IN_PUMA_ACQUIRING_ONLY("MPA-loggedIn-PUMA-acquiringOnly", PropertiesUtil.MPA.getProperty("mail.ads.puma950.gmail")),
    MPA_LOGGED_IN_PUMA_BOTH("MPA-loggedIn-pumaBoth", PropertiesUtil.MPA.getProperty("mail.ads.puma_both")),
    MPA_LOGGED_IN_PUMA_TERMINAL_ONLY_FI("MPA-loggedIn-pumaTerminal", PropertiesUtil.MPA.getProperty("mail.ads.puma_terminal_only")),
    //    TNP
    MPA_LOGGED_IN_TNP11("MPA-loggedIn-TNP", PropertiesUtil.MPA.getProperty("mail.ads.tnp11.gmail")),
    //    BAU = MyCC
    MPA_LOGGED_IN_BAU("MPA-loggedIn-BAU", PropertiesUtil.MPA.getProperty("mail.ads.bau.gmail")),
    //    MEPO
    MPA_LOGGED_IN_MEPO("MPA-loggedIn-MEPO", PropertiesUtil.MPA.getProperty("mail.ads.mepo.gmail")),
    //    CVR
    MPA_LOGGED_IN_CVR_SINGLE_ADMIN("MPA-loggedIn-CVR-singleAdmin", PropertiesUtil.MPA.getProperty("email.cvr.singleAdmin")),
    MPA_LOGGED_IN_CVR_MULTIPLE_ADMINS("MPA-loggedIn-CVR-multipleAdmins", PropertiesUtil.MPA.getProperty("email.cvr.multipleAdmins"));
    //    OTHER
    //...

    private final String name;
    private final String username;

    AndroidSnapshot(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public static AndroidSnapshot fromName(String name) {
        for (AndroidSnapshot snapshot : values()) {
            if (snapshot.name.equalsIgnoreCase(name)) {
                return snapshot;
            }
        }

        throw new IllegalArgumentException(logError("No Snapshot found matching name: " + name));
    }

    // Works only if emulator is running
    public boolean exists() throws IOException, InterruptedException {
        Process getSnapshots = new ProcessBuilder(EnvUtil.ADB, "emu", "avd", "snapshot", "list").start();
        getSnapshots.waitFor();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getSnapshots.getInputStream()))) {
            List<String> snapshots = reader.lines().toList();
            for (String snapshot : snapshots) {
                if (snapshot.contains(this.name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void create(MpaAndroidDriver driver) throws IOException {
        switch (this) {
            case NO_MPA:
                driver.installApp(PathKey.IO_APPIUM_SETTINGS_V5.resolve().asPath(), "io.appium.settings");

                driver.quit();
                break;
            case MPA_LOGGED_OUT:
                if (!driver.isAppInstalled("io.appium.settings")) {
                    driver.installApp(PathKey.IO_APPIUM_SETTINGS_V5.resolve().asPath(), "io.appium.settings");
                }
                driver.installMpa();

                driver.quit();
                break;
            default:
                driver.quit();
                throw new RuntimeException(logError(String.format("create() non applicable to snapshot: %s - Valid options: [%s, %s]",
                        this,
                        NO_MPA,
                        MPA_LOGGED_OUT)));
        }
    }

    public void save() throws IOException {
        new ProcessBuilder(EnvUtil.ADB, "emu", "avd", "snapshot", "save", this.name).start();
    }

    public void load() throws IOException {
        new ProcessBuilder(EnvUtil.ADB, "emu", "avd", "snapshot", "load", this.name).start();
    }

    public void delete() throws IOException {
        new ProcessBuilder(EnvUtil.ADB, "emu", "avd", "snapshot", "delete", this.name).start();
    }
}

