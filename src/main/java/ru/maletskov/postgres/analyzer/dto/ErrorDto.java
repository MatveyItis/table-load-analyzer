package ru.maletskov.postgres.analyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private String code;

    private String errorMessage;

    private String errorPath;

    private String stackTrace;
}
