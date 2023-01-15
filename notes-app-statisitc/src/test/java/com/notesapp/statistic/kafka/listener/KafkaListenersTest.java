package com.notesapp.statistic.kafka.listener;

import static com.notesapp.statistic.model.event.EventType.READ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notesapp.statistic.kafka.consumer.KafkaConsumerConfiguration;
import com.notesapp.statistic.model.event.NoteAccessEvent;
import com.notesapp.statistic.model.view.NoteView;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {KafkaConsumerConfiguration.class})
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092", "partitions=1"})
class KafkaListenersTest {

    static final String TEST_TOPIC = "testTopic";

    Producer<String, NoteAccessEvent> producer;
    KafkaMessageListenerContainer<String, NoteAccessEvent> container;

    @BeforeEach
    void setUp() {
        producer = new DefaultKafkaProducerFactory<>(producerConfig(), new StringSerializer(), new JsonSerializer<NoteAccessEvent>(new ObjectMapper())).createProducer();
        DefaultKafkaConsumerFactory<String, NoteAccessEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties(), new StringDeserializer(),
                new JsonDeserializer<>(NoteAccessEvent.class));
        ContainerProperties containerProperties = new ContainerProperties("testTopic");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Test
    void testListener() throws InterruptedException {
        @SuppressWarnings("unchecked") AcknowledgingConsumerAwareMessageListener<String, String> messageListener = (AcknowledgingConsumerAwareMessageListener<String, String>) container
                .getContainerProperties().getMessageListener();
        container.getContainerProperties()
                .setMessageListener((AcknowledgingConsumerAwareMessageListener<String, String>) (data, acknowledgment, consumer) -> {
                    messageListener.onMessage(data, acknowledgment, consumer);
                    System.out.println("hello there: " + data);
                });
        producer.send(new ProducerRecord<>(TEST_TOPIC, new NoteAccessEvent(new NoteView("title", "content"), READ)));
        Thread.sleep(1000);
    }

    static Map<String, Object> producerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    Map<String, Object> consumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
    }
}