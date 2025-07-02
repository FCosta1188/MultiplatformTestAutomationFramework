package eu.nets.test.core.enums;

import static eu.nets.test.util.AllureUtil.logError;

@Deprecated
public enum MpaPlatform {
    ANDROID("Android"),
    IOS("iOS");

    private final String name;

    MpaPlatform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MpaPlatform fromName(String name) {
        for (MpaPlatform platform : values()) {
            if (platform.name.equalsIgnoreCase(name)) {
                return platform;
            }
        }

        throw new IllegalArgumentException(logError("No MpaPlatform found matching name: " + name));
    }
}
