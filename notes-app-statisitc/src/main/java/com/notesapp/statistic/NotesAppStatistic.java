package com.notesapp.statistic;

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

    @Bean
    public CommandLineRunner runner(KafkaTemplate<String, String> kafkaTemplate) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                kafkaTemplate.send("testTopic", String.format("hello to kafka! - [%s]", i));
            }
        };
    }
}