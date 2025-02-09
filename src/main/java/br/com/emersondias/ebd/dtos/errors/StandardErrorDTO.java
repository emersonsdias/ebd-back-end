package br.com.emersondias.ebd.dtos.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class StandardErrorDTO implements Serializable {

    @Schema(description = "UTC timestamp when the error occurred", example = "2025-02-03T23:29:48.072Z")
    private final Instant timestamp;
    @Schema(description = "HTTP status code of the error", example = "400")
    private final Integer status;
    @Schema(description = "Error type or category", example = "Unexpected Error")
    private final String error;
    @Schema(description = "Detailed error message", example = "An unexpected error occurred. Please try again later or contact the system administrator")
    private final String message;
    @Schema(description = "Request URI where the error occurred", example = "/api/resource/123")
    private final String path;
    @Builder.Default
    @Schema(description = "Additional details about the error", example = "[\"Invalid email format\"]")
    private final List<String> additionalInfo = new ArrayList<>();

    public void addAdditionalData(String message) {
        this.additionalInfo.add(message);
    }

}