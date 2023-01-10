package com.notesapp.notesapp.service;


import static com.notesapp.notesapp.model.event.EventType.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.notesapp.notesapp.kafka.KafkaProducerConfiguration;
import com.notesapp.notesapp.model.event.NoteAccessEvent;
import com.notesapp.notesapp.model.view.NoteView;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("test")
@SpringBootTest(classes = {KafkaNotesNotificationsService.class, KafkaProducerConfiguration.class})
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092", "partitions=1"})
class KafkaNotesNotificationsServiceTest {

    @Autowired
    KafkaNotesNotificationsService kafkaNotesNotificationsService;

    private KafkaMessageListenerContainer<String, NoteAccessEvent> container;
    private BlockingQueue<ConsumerRecord<String, NoteAccessEvent>> records;

    @BeforeEach
    void setUp() {
        DefaultKafkaConsumerFactory<String, NoteAccessEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties(), new StringDeserializer(),
                new JsonDeserializer<>(NoteAccessEvent.class));
        ContainerProperties containerProperties = new ContainerProperties("testTopic");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, NoteAccessEvent>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, 1);
    }

    @AfterEach
    void tearDown() {
        container.stop();
    }

    @Test
    void sendEvent() throws InterruptedException {
        var view = new NoteView(1L, "title", "content");
        var event = new NoteAccessEvent(view, CREATED);
        kafkaNotesNotificationsService.sendEvent(event);

        ConsumerRecord<String, NoteAccessEvent> message = records.poll(500, TimeUnit.MILLISECONDS);
        assertNotNull(message);
        assertEquals(event, message.value());
    }

    Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
    }
}