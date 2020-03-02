package pl.kkurczewski.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(ports = 9092)
class MessageConsumerTest {

    @Autowired
    Environment environment;

    @Autowired
    MessageConsumer messageConsumer;

    @BeforeEach
    void setUp() {
        messageConsumer.consumedEvents.clear();
    }

    @Test
    void manualProducer() throws InterruptedException, TimeoutException, ExecutionException {

        String topic = Objects.requireNonNull(environment.getProperty("app.topic.message"));
        String broker = environment.getProperty("app.kafka.broker");

        Properties producerProps = new Properties();
        producerProps.put(BOOTSTRAP_SERVERS_CONFIG, broker);
        producerProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // NOTE: has many import collisions

        var producer = new KafkaProducer<String, Message>(producerProps);
        producer.send(new ProducerRecord<>(topic, "test key", new Message("test value"))).get(10, SECONDS);

        SECONDS.sleep(1);

        assertThat(messageConsumer.consumedEvents).containsExactly(new Message("test value"));
    }

    @Test
    void manualProducer_string() throws InterruptedException, TimeoutException, ExecutionException, JsonProcessingException {

        String topic = Objects.requireNonNull(environment.getProperty("app.topic.message"));
        String broker = environment.getProperty("app.kafka.broker");

        Properties producerProps = new Properties();
        producerProps.put(BOOTSTRAP_SERVERS_CONFIG, broker);
        producerProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var producer = new KafkaProducer<String, String>(producerProps);
        String payload = new ObjectMapper().writeValueAsString(new Message("test value"));

        producer.send(new ProducerRecord<>(topic, "test key", payload)).get(10, SECONDS);

        SECONDS.sleep(1);

        assertThat(messageConsumer.consumedEvents).containsExactly(new Message("test value"));
    }

}
