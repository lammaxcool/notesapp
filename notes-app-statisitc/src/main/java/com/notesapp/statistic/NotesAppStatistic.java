package com.notesapp.statistic;

import com.notesapp.statistic.model.event.EventType;
import com.notesapp.statistic.model.event.NoteAccessEvent;
import com.notesapp.statistic.model.view.NoteView;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NotesAppStatistic {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppStatistic.class, args);
    }
}