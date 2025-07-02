package eu.nets.test.core.exceptions;

import eu.nets.test.util.PropertiesUtil;

import static eu.nets.test.util.AllureUtil.logError;

public class UnsupportedPlatformException extends UnsupportedOperationException {
    public UnsupportedPlatformException() {
        super(logError("Platform not supported: " + PropertiesUtil.ENV.getProperty("PLATFORM")));
    }
}
