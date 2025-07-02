package eu.nets.test.util;

import eu.nets.test.core.enums.MpaLanguage;
import eu.nets.test.core.enums.PathKey;
import eu.nets.test.core.exceptions.LokaliseMismatchException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static eu.nets.test.util.AllureUtil.logError;
import static eu.nets.test.util.AllureUtil.logInfo;

public final class LokaliseUtil {
    private static final String API_BASE_URL = PropertiesUtil.MPA.getProperty("lokalise.baseUrl");
    private static final String PROJECT_ID = PropertiesUtil.MPA.getProperty("lokalise.projectId");
    private static final String API_PROJECT_URL = API_BASE_URL + "/projects/" + PROJECT_ID;
    private static final String API_URL_SYNC = API_PROJECT_URL + "/files/download";
    private static final String API_URL_ASYNC = API_PROJECT_URL + "/files/async-download";
    private static final String API_TOKEN_SYNC = PropertiesUtil.MPA.getProperty("lokalise.apiTokenSync");
    private static final String API_TOKEN_ASYNC = PropertiesUtil.MPA.getProperty("lokalise.apiTokenAsync");

    private static Path BUNDLE_FILEPATH;

    public static Path getBundleFilepath() {
        return BUNDLE_FILEPATH;
    }

    public static void setBundleFilepath(String bundleFilename) {
        BUNDLE_FILEPATH = PathKey.LOKALISE_BUNDLE.resolve().asPath().resolve(bundleFilename);
    }

    public static void downloadBundle(boolean asyncApi) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject payload = new JSONObject();
            payload.put("format", "xml");
            payload.put("original_filenames", true);
            payload.put("export_sort", "a_z");

            URI uri;
            String token;
            if (asyncApi) {
                uri = URI.create(API_URL_ASYNC);
                token = API_TOKEN_ASYNC;
            } else {
                uri = URI.create(API_URL_SYNC);
                token = API_TOKEN_SYNC;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("X-Api-Token", token)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            String bundleUrl = null;
            if (asyncApi) {
                String processId = jsonResponse.getString("process_id");
                String statusUrl = API_PROJECT_URL + "/processes/" + processId;

                int maxTries = 10;
                int waitMs = 2000;

                for (int i = 0; i < maxTries; i++) {
                    EnvUtil.safeSleep(waitMs);

                    HttpRequest pollRequest = HttpRequest.newBuilder()
                            .uri(URI.create(statusUrl))
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header("X-Api-Token", API_TOKEN_ASYNC)
                            .build();

                    HttpResponse<String> pollResponse = client.send(pollRequest, HttpResponse.BodyHandlers.ofString());
                    JSONObject pollJson = new JSONObject(pollResponse.body());
                    String status = pollJson.getJSONObject("process").getString("status");

                    if (status.equalsIgnoreCase("finished")) {
                        bundleUrl = pollJson.getJSONObject("process").getJSONObject("details").getString("download_url");
                        break;
                    }
                }
            } else {
                bundleUrl = jsonResponse.getString("bundle_url");
            }

            if (bundleUrl == null) {
                throw new RuntimeException(logError("Bundle download timed out or failed - async API: " + asyncApi));
            }

            HttpRequest downloadRequest = HttpRequest.newBuilder()
                    .uri(URI.create(bundleUrl))
                    .build();

            setBundleFilepath(bundleUrl.substring(bundleUrl.lastIndexOf("/") + 1));
            HttpResponse<Path> downloadResponse = client.send(downloadRequest, HttpResponse.BodyHandlers.ofFile(getBundleFilepath()));

            logInfo("Bundle download response: " + downloadResponse.toString());
            logInfo("Bundle file saved to: " + downloadResponse.body().toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(logError(e.getMessage()));
        }
    }

    public static JSONObject readLanguageFile(MpaLanguage targetLanguage) throws IOException {
        Path zipPath = getBundleFilepath();

        if (!Files.exists(zipPath) || Files.size(zipPath) < 1000) {
            throw new IOException(logError("Invalid ZIP file downloaded (low size or not exists): " + zipPath));
        }

        try (ZipFile zipFolder = new ZipFile(zipPath.toFile())) {

            String targetEntryPrefix = targetLanguage.getLokaliseXmlFolderName() + "/Localizable.xml";

            ZipEntry targetEntry = zipFolder.stream()
                    .filter(entry -> entry.getName().equalsIgnoreCase(targetEntryPrefix))
                    .findFirst()
                    .orElseThrow(() -> new FileNotFoundException("File Localizable.xml not found in folder: " + targetLanguage.getIsoCode()));

            try (InputStream is = zipFolder.getInputStream(targetEntry)) {
                String xmlText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                return XML.toJSONObject(xmlText);
            }
        }
    }

    public static Map<MpaLanguage, Map<String, String>> getBundle() throws IOException {
        Map<MpaLanguage, Map<String, String>> bundle = new TreeMap<>();

        for (MpaLanguage lang : MpaLanguage.values()) {
            JSONObject langJson = readLanguageFile(lang);
            JSONArray strings = langJson.getJSONObject("resources").getJSONArray("string");

            Map<String, String> langMap = new HashMap<>();
            for (int i = 0; i < strings.length(); i++) {
                JSONObject entry = strings.getJSONObject(i);
                String name = entry.getString("name");
                String content = entry.has("content") ? String.valueOf(entry.get("content")) : "";

                langMap.put(name, content);
            }

            bundle.put(lang, langMap);
        }

        return bundle;
    }

    public static String getContentByName(JSONArray strings, String targetName) {
        for (int i = 0; i < strings.length(); i++) {
            JSONObject entry = strings.getJSONObject(i);
            if (entry.getString("name").equals(targetName)) {
                return entry.getString("content");
            }
        }
        throw new IllegalArgumentException(logError("No entry found with name: " + targetName));
    }

    public static void match(WebElement element, String lokaliseKey, MpaLanguage language) throws IOException, LokaliseMismatchException {
        if (!element.getText().trim().equalsIgnoreCase(getBundle().get(language).get(lokaliseKey).trim())) {
            throw new LokaliseMismatchException(element, lokaliseKey, getBundle().get(language).get(lokaliseKey), language);
        }
    }
}