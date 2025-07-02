package eu.nets.test.util;

import eu.nets.test.core.enums.PathKey;
import io.qameta.allure.Allure;
import io.qameta.allure.Allure.ThrowableRunnableVoid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import static eu.nets.test.util.EnvUtil.emptyFolder;

// Logs and reports
public final class AllureUtil {
    public static Path ALLURE_RESULTS_DIR = PathKey.ALLURE_RESULTS.resolve().asPath();
    public static Path CUSTOM_REPORTS_DIR = PathKey.CUSTOM_ALLURE_REPORTS.resolve().asPath();

    public static String logStep(String message) {
        Allure.step(message);
        return message;
    }

    public static String logStep(String title, ThrowableRunnableVoid step) {
        Allure.step(title, step);
        return title;
    }

    public static String logInfo(String message) {
        attachText("INFO", message);
        return message;
    }

    public static String logWarning(String message) {
        attachText("WARNING", message);
        return message;
    }

    public static String logError(String message) {
        attachText("ERROR", message);
        return message;
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

    public static void attachHtml(String name, String html) {
        Allure.addAttachment(name, "text/html", html);
    }

    public static void attachImage(String name, String format, byte[] screenshotBytes) {
        Allure.addAttachment(name, "image/" + format, new ByteArrayInputStream(screenshotBytes), "." + format);
    }

    public static void emptyAllureResults() throws IOException {
        //        Path historyDir = ALLURE_RESULTS_DIR.resolve("history");
        //        Path historyBackupDir = CUSTOM_REPORTS_DIR.resolve("history-backup") ;
        //
        //        if (!Files.exists(historyBackupDir)) {
        //            Files.createDirectories(historyBackupDir);
        //        } else {
        //            emptyFolder(historyBackupDir, true);
        //        }
        //
        //        if (historyDir.toFile().exists()) {
        //            EnvUtil.copyFolder(historyDir, historyBackupDir);
        //        }

        emptyFolder(ALLURE_RESULTS_DIR, true);

        //        if (historyBackupDir.toFile().exists()) {
        //            EnvUtil.copyFolder(historyBackupDir, historyDir);
        //        }
    }

    public static void emptyCustomAllureReports() throws IOException {
        emptyFolder(CUSTOM_REPORTS_DIR, true);
    }

    public static Path createCustomReportFolder(String flowName) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String customFolderName = "report-" + flowName + "-" + formatter.format(now);

        return CUSTOM_REPORTS_DIR.resolve(customFolderName);
    }

    public static void generateCustomReport(Path customReportDir) throws IOException {
        try {
            if (EnvUtil.isWin()) {
                new ProcessBuilder(
                        "cmd", "/c", "allure", "generate",
                        ALLURE_RESULTS_DIR.toString(),
                        "-o", customReportDir.toString(),
                        "--clean" //overwrite if dir already exists
                ).start().waitFor();
            } else if (EnvUtil.isMacOs()) {
                new ProcessBuilder(
                        "allure", "generate",
                        ALLURE_RESULTS_DIR.toString(),
                        "-o", customReportDir.toString(),
                        "--clean" //overwrite if dir already exists
                ).start().waitFor();
                ;
            } else {
                throw new RuntimeException(logError("OS not supported: " + EnvUtil.OS));
            }
        } catch (Exception e) {
            throw new IOException(logError("Failed to generate report - expected output dir: " + customReportDir));
        }
    }
}
