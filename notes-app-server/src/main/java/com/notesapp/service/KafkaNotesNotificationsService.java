package com.notesapp.service;

import com.notesapp.kafka.KafkaApplicationProperties;
import com.notesapp.model.event.NoteAccessEvent;
import com.notesapp.model.view.NoteView;
import com.notesapp.model.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotesNotificationsService implements NotificationService<NoteView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaNotesNotificationsService.class);

    private final KafkaApplicationProperties kafkaApplicationProperties;
    private final KafkaTemplate<String, NoteAccessEvent> kafkaTemplate;

    public KafkaNotesNotificationsService(KafkaApplicationProperties kafkaApplicationProperties, KafkaTemplate<String, NoteAccessEvent> kafkaTemplate) {
        this.kafkaApplicationProperties = kafkaApplicationProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onCreate(NoteView entity) {
        var event = new NoteAccessEvent(entity, EventType.CREATED);
        sendEvent(event);
    }

    @Override
    public void onRead(NoteView entity) {
        NoteAccessEvent event = new NoteAccessEvent(entity, EventType.READ);
        sendEvent(event);
    }

    @Override
    public void onUpdate(NoteView entity) {
        NoteAccessEvent event = new NoteAccessEvent(entity, EventType.UPDATED);
        sendEvent(event);
    }

    @Override
    public void onDelete(NoteView entity) {
        NoteAccessEvent event = new NoteAccessEvent(entity, EventType.DELETED);
        sendEvent(event);
    }

    public void sendEvent(NoteAccessEvent event) {
        var message = createMessageWithPayload(event);
        kafkaTemplate.send(message);
        LOGGER.info("Event sent: {}", event);
    }

    private Message<NoteAccessEvent> createMessageWithPayload(NoteAccessEvent event) {
        return MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, kafkaApplicationProperties.topic())
                .build();
    }
}