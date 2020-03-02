package pl.kkurczewski.message;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

public class Message {

    private String message;

    public Message() {
        // for marshalling
    }

    public Message(String message) {
        this.message = requireNonNull(message);
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message other = (Message) o;
        return message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return hash(message);
    }
}
