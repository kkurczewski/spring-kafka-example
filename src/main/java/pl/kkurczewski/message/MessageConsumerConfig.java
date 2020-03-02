package pl.kkurczewski.message;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.UUID;

@Configuration
public class MessageConsumerConfig {

    @Value("${app.kafka.broker}")
    String kafkaBroker;

    @Bean
    public ConsumerFactory<String, Message> messageConsumerFactory() {

        JsonDeserializer<Message> deserializer = new JsonDeserializer<>(Message.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("pl.kkurczewski.*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> properties = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker,
                ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> messageContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }
}
