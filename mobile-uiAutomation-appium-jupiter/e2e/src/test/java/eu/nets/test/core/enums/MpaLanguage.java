package eu.nets.test.core.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static eu.nets.test.util.AllureUtil.logError;

public enum MpaLanguage {
        DANISH("da", true),
        ENGLISH("en", true),
        ESTONIAN("et_EE", true),
        FINNISH("fi", true),
        FRENCH("fr", true),
        GERMAN("de", true),
        ITALIAN("it", true),
        LATVIAN("lv_LV", true),
        LITHUANIAN("lt_LT", true),
        NORWEGIAN("nb", true),
        POLISH("pl", false),
        SPANISH("es", false),
        SWEDISH("sv", true);

    public static final MpaLanguage DEFAULT = ENGLISH;

    private final String isoCode;
    private final boolean isSupportedByMpa;
    private Map<String, String> dictionary;
    private String lokaliseXmlFolderName;
    private Locale region;
    private final String language;
    private final String country;

    MpaLanguage(String isoCode, boolean isSupportedByMpa) {
        this.isoCode = isoCode;
        this.isSupportedByMpa = isSupportedByMpa;
        setDictionary();
        setLokaliseXmlFolderName();
        setRegion();
        this.language = region.getLanguage();
        this.country = region.getCountry();
    }

    public void setDictionary() {
        ObjectMapper objectMapper = new ObjectMapper();
        String dictionaryFilename = this.isoCode.substring(0, 2) + ".json";
        Path dictionaryFilepath = Paths.get(PathKey.DICTIONARIES.resolve().asString(), dictionaryFilename);

        try {
            this.dictionary = objectMapper.readValue(
                    new File(dictionaryFilepath.toString()),
                    new TypeReference<Map<String, String>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException(logError("Failed to load dictionary for language ISO code: " + isoCode), e);
        }
    }

    public void setLokaliseXmlFolderName() {
        this.lokaliseXmlFolderName = "values";

        if (!this.isoCode.equals("en")) {
            this.lokaliseXmlFolderName = this.lokaliseXmlFolderName + "-" + this.isoCode.replace("_", "-r");
        }
    }

    public void setRegion() {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.toString().contains(this.isoCode) && !locale.getCountry().isBlank()) {
                this.region = locale;
                return;
            }
        }

        throw new IllegalArgumentException(logError("No Locale found matching ISO language code: " + isoCode));
    }

    public String getIsoCode() {
        return this.isoCode;
    }

    public boolean isSupportedByMpa() {
        return isSupportedByMpa;
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }

    public String getLokaliseXmlFolderName() {
        return this.lokaliseXmlFolderName;
    }

    public Locale getRegion() {
        return this.region;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getCountry() {
        return this.country;
    }

    public static MpaLanguage fromIsoCode(String isoCode) {
        for (MpaLanguage lang : values()) {
            if (lang.isoCode.equalsIgnoreCase(isoCode)) {
                return lang;
            }
        }

        throw new IllegalArgumentException(logError("No Language found matching ISO code: " + isoCode));
    }


    public String capitalize(String dictionaryKey) {
        return Optional.ofNullable(this.dictionary.get(dictionaryKey))
                .map(text -> {
                    String[] parts = text.split(" ");
                    for (int i = 0; i < parts.length; i++) {
                        if (!parts[i].isBlank()) {
                            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                        }
                    }
                    return String.join(" ", parts);
                })
                .orElseThrow(() -> new IllegalArgumentException(
                        logError(String.format("No translation found for key [%s] and language [%s]", dictionaryKey, this))
                ));
    }
}
