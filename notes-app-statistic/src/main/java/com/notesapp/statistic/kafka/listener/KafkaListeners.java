package com.notesapp.statistic.kafka.listener;

import com.notesapp.model.event.NoteAccessEvent;
import com.notesapp.statistic.kafka.NotesAccessEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListeners.class);

    private final NotesAccessEventsService notesAccessEventsService;

    public KafkaListeners(NotesAccessEventsService notesAccessEventsService) {
        this.notesAccessEventsService = notesAccessEventsService;
    }

    @KafkaListener(topics = "${application.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NoteAccessEvent event) {
        notesAccessEventsService.onEvent(event);
        LOGGER.info("Got new notification: {}", event);
    }
}
