package eu.nets.test.core.enums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.nets.test.core.exceptions.UnsupportedPlatformException;
import eu.nets.test.util.EnvUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static eu.nets.test.util.AllureUtil.logError;

public enum MpaLanguage {
//        DANISH("da", true),
//        ENGLISH("en", true),
//        ESTONIAN("et_EE", true),
//        FINNISH("fi", true),
//        FRENCH("fr", true),
//        GERMAN("de", true),
//        ITALIAN("it", true),
//        LATVIAN("lv_LV", true),
//        LITHUANIAN("lt_LT", true),
//        NORWEGIAN("nb", true),
//        POLISH("pl", false),
//        SPANISH("es", false),
//        SWEDISH("sv", true);

          ENGLISH("en", true);

    public static final MpaLanguage FALLBACK = ENGLISH;
    public static final int SIZE = MpaLanguage.values().length;
    public static final int SUPPORTED_LANGUAGES_COUNT = (int) Stream.of(MpaLanguage.values())
            .filter(MpaLanguage::isSupportedByMpa)
            .count();

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
        String dictionaryFilename = this.isoCode + ".json";
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
        return this.isSupportedByMpa;
    }

    public Map<String, String> getDictionary() {
        return this.dictionary;
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

    public static MpaLanguage fromNativeName(String nativeName) {
        for (MpaLanguage lang : values()) {
            if (lang.nativeName().equalsIgnoreCase(nativeName)) {
                return lang;
            }
        }

        throw new IllegalArgumentException(logError("No Language found matching nativeName: " + nativeName));
    }

    public static String capitalizeText(String text, boolean eachWord) {
        if(eachWord){
            return Optional.ofNullable(text)
                    .map(t -> {
                        String[] parts = t.split(" ");
                        for (int i = 0; i < parts.length; i++) {
                            if (!parts[i].isBlank()) {
                                if(parts[i].contains("(")) {
                                    parts[i] = parts[i].charAt(0) + String.valueOf(parts[i].charAt(1)).toUpperCase() + parts[i].substring(2);
                                } else {
                                    parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                                }
                            }
                        }
                        return String.join(" ", parts);
                    })
                    .orElseThrow();

        } else {
            return Optional.ofNullable(text)
                    .map(t -> {
                        String[] parts = text.split(" ");
                        if (!parts[0].isBlank()) {
                            parts[0] = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);
                        }
                        return String.join(" ", parts);
                    })
                    .orElseThrow();
        }
    }

    public String capitalizeDictionaryEntry(String dictionaryKey, boolean eachWord) {
        if(eachWord){
            return Optional.ofNullable(this.dictionary.get(dictionaryKey))
                    .map(text -> {
                        String[] parts = text.split(" ");
                        for (int i = 0; i < parts.length; i++) {
                            if (!parts[i].isBlank()) {
                                if(parts[i].contains("(")) {
                                    parts[i] = parts[i].charAt(0) + String.valueOf(parts[i].charAt(1)).toUpperCase() + parts[i].substring(2);
                                } else {
                                    parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                                }
                            }
                        }
                        return String.join(" ", parts);
                    })
                    .orElseThrow(() -> new IllegalArgumentException(
                            logError(String.format("No translation found for key [%s] and language [%s]", dictionaryKey, this))
                    ));
        } else {
            return Optional.ofNullable(this.dictionary.get(dictionaryKey))
                    .map(text -> {
                        String[] parts = text.split(" ");
                        if (!parts[0].isBlank()) {
                            parts[0] = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);
                        }
                        return String.join(" ", parts);
                    })
                    .orElseThrow(() -> new IllegalArgumentException(
                            logError(String.format("No translation found for key [%s] and language [%s]", dictionaryKey, this))
                    ));
        }
    }

    public String nativeName() {
        return this.getDictionary().get(this.toString().toLowerCase());
    }

    public String label(MpaLanguage selectedAppLanguage) {
        if (this.equals(selectedAppLanguage)) {
            if(EnvUtil.isAndroid()) {
                return this.getDictionary().get("default");
            } else if (EnvUtil.isIos()) {
                return "default";
            } else {
                throw new UnsupportedPlatformException();
            }
        } else {
            return selectedAppLanguage.getDictionary().get(this.toString().toLowerCase());
        }
    }
}
