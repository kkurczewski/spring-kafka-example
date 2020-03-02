package pl.kkurczewski;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class DefaultConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        // defined to stop Spring complaint about missing bean
        return new ConcurrentKafkaListenerContainerFactory<>();
    }
}
