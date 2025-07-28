package eu.nets.test.util;

import eu.nets.test.core.enums.PathKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import static eu.nets.test.util.AllureUtil.logError;

public final class PropertiesUtil {

    public static final Properties CONFIG = loadDotProperties("properties/config.properties");
    public static final Properties MPA = loadDotProperties("properties/mpa.properties");
    public static final Properties ENV = loadDotEnv();

    private static Properties loadDotProperties(String fileName) {
        Properties properties = new Properties();

        try (InputStream input = EnvUtil.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException(logError("File not found: " + fileName));
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(logError("Error reading file: " + e.getMessage()));
        }

        return properties;
    }

    public static String getProperty(String fileName, String key) {
        Properties properties = loadDotProperties(fileName);

        return properties.getProperty(key);
    }

    public static String getProperty(String fileName, String key, Charset charset) {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(new InputStreamReader(fis, charset));
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(logError("Error reading file: " + e.getMessage()));
        }
    }

    private static Properties loadDotEnv() {
        Properties rawEnv = new Properties();
        Properties filtered = new Properties();

        try {
            FileInputStream fis;
            if (EnvUtil.isWin() || EnvUtil.isMac()) {
                fis = new FileInputStream(new File(PathKey.E2E_MODULE_ROOT.toString(), ".env"));
            } else {
                throw new RuntimeException(logError("OS not supported: " + EnvUtil.OS));
            }
            rawEnv.load(fis);
        } catch (Exception e) {
            throw new RuntimeException(logError("Failed to load .env file: " + e.getMessage()));
        }

        String platform = rawEnv.getProperty("PLATFORM").toLowerCase().trim();
        if (platform.isBlank()) {
            throw new RuntimeException(logError("PLATFORM value missing in .env file"));
        }

        // Rename keys from <PLATFORM>_<property> to <property> (es: ANDROID_emulatorDeviceName -> emulatorDeviceName)
        for (String key : rawEnv.stringPropertyNames()) {
            if (key.startsWith(platform.toUpperCase() + "_")) {
                String newKey = key.substring(platform.length() + 1);
                filtered.setProperty(newKey, rawEnv.getProperty(key));
            }
        }

        // Add the PLATFORM key to the filtered properties
        filtered.setProperty("PLATFORM", platform);

        return filtered;
    }
}