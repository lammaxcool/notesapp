package com.notesapp.statistic.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("application.kafka")
public record KafkaApplicationProperties(
        @NotBlank String topic,
        @NotBlank String bootstrapServers
) {
}