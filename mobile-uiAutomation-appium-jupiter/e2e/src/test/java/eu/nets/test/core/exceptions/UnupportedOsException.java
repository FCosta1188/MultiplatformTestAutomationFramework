package eu.nets.test.core.exceptions;

import eu.nets.test.util.EnvUtil;

import static eu.nets.test.util.AllureUtil.logError;

public class UnupportedOsException extends UnsupportedOperationException {
    public UnupportedOsException() {
        super(logError("OS not supported: " + EnvUtil.OS));
    }
}
