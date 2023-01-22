package com.notesapp.statistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NotesAppStatistic {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppStatistic.class, args);
    }
}