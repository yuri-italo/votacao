package dev.yuri.votacao.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ErroResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> errors;

    @AllArgsConstructor
    @Getter
    public static class FieldError {
        private String field;
        private String message;
    }
}
