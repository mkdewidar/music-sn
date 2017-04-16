package musicss.client.event;

import javafx.event.Event;
import javafx.event.EventType;
import musicss.protocol.message.Request;

/**
 * Custom events that are fired by UI components.
 */
public class AppEvent extends Event {
    public static EventType<AppEvent> Type = new EventType<>(Event.ANY, "AppEvent.ANY");

    public AppEvent() {
        super(Type);
    }


    /**
     * An event representing an attempt to login.
     */
    public static class Login extends Event {
        public static EventType<Login> Type = new EventType<>(AppEvent.ANY, "AppEvent.Login");

        public Request.Login request;

        public Login() {
            super(Type);
        }
    }
}
