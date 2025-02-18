package br.com.emersondias.ebd.exceptions;

import lombok.Getter;

@Getter
public class ReportGenerationException extends Exception {

    private final String reportFile;

    public ReportGenerationException(String message, Throwable cause, String reportFile) {
        super(message, cause);
        this.reportFile = reportFile;
    }
}
