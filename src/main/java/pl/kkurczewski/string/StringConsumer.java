package pl.kkurczewski.string;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StringConsumer {

    List<String> consumedEvents = new ArrayList<>();

    @KafkaListener(topics = "${app.topic.string}", containerFactory = "stringContainerFactory")
    public void consumeMessage(String message) {
        consumedEvents.add(message);
    }
}
