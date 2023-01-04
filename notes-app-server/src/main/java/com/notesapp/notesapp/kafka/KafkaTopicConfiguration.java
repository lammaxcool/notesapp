package com.notesapp.notesapp.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaApplicationProperties.class)
public class KafkaTopicConfiguration {

    private final KafkaApplicationProperties kafkaProperties;

    public KafkaTopicConfiguration(KafkaApplicationProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(kafkaProperties.topic())
                .partitions(2)
                .replicas(1)
                .build();
    }
}
