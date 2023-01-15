package com.notesapp.statistic.kafka.listener;

import com.notesapp.statistic.model.event.NoteAccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "${application.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NoteAccessEvent event) {

        LOGGER.info("Got new notification: {}", event);
    }
}
