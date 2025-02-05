package br.com.emersondias.ebd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;

public class LogHelper {

    private final Logger LOG;

    private LogHelper(Class<?> classType) {
        this.LOG = LogManager.getLogger(classType);
    }

    public static LogHelper getInstance() {
        Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        return new LogHelper(callerClass);
    }

    public void debug(Object message) {
        this.LOG.debug(message);
    }

    public void debug(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.debug(message.concat(reference));
    }

    public void info(Object message) {
        this.LOG.info(message);
    }

    public void info(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.info(message.concat(reference));
    }

    public void error(Object message) {
        this.LOG.error(message);
    }

    public void error(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.error(message.concat(reference));
    }

    public void stackTrace(String message, Throwable throwable) {
        this.LOG.error(message, throwable);
    }
}
