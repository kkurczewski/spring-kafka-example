package pl.kkurczewski.string;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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
class StringConsumerTest {

    @Autowired
    Environment environment;

    @Autowired
    StringConsumer stringConsumer;

    @Test
    void manualProducer() throws InterruptedException, TimeoutException, ExecutionException {

        String topic = Objects.requireNonNull(environment.getProperty("app.topic.string"));
        String broker = environment.getProperty("app.kafka.broker");

        Properties producerProps = new Properties();
        producerProps.put(BOOTSTRAP_SERVERS_CONFIG, broker);
        producerProps.put(ACKS_CONFIG, "all");
        producerProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var producer = new KafkaProducer<String, String>(producerProps);
        producer.send(new ProducerRecord<>(topic, "test key", "test value")).get(10, SECONDS);

        SECONDS.sleep(1);

        assertThat(stringConsumer.consumedEvents).containsExactly("test value");
    }
}
