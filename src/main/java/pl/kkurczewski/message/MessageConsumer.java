package pl.kkurczewski.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageConsumer {

    List<Message> consumedEvents = new ArrayList<>();

    @KafkaListener(topics = "${app.topic.message}", containerFactory = "messageContainerFactory")
    public void consumeMessage(Message message) {
        consumedEvents.add(message);
    }
}
