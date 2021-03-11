package ru.maletskov.postgres.analyzer.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DbInfoDto {

    private Set<SchemaDto> schemas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchemaDto {

        private String name;

        private Set<String> tables;
    }
}
