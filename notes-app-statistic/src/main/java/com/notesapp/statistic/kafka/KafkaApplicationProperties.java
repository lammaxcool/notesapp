package com.notesapp.statistic.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties("application.kafka")
public record KafkaApplicationProperties(
        @NotBlank String topic,
        @NotBlank String bootstrapServers
) {
}