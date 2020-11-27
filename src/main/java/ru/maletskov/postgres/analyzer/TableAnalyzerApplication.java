package ru.maletskov.postgres.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TableAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableAnalyzerApplication.class, args);
    }

}
