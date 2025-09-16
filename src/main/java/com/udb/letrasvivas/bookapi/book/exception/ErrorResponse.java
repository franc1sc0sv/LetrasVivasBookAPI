package com.udb.letrasvivas.bookapi.book.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response format")
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Bad Request")
    private String error;

    @Schema(description = "Error message", example = "Validation failed")
    private String message;

    @Schema(description = "Detailed error information")
    private Map<String, String> details;

    @Schema(description = "List of validation errors")
    private List<ValidationError> validationErrors;

    @Schema(description = "Request path that caused the error", example = "/api/books")
    private String path;

    @Schema(description = "Additional error context")
    private String context;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Individual validation error")
    public static class ValidationError {

        @Schema(description = "Field name that failed validation", example = "title")
        private String field;

        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;

        @Schema(description = "Error message", example = "Title is required")
        private String message;
    }
}
