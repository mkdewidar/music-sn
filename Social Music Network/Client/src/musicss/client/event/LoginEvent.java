package musicss.client.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Represents a Login Event
 */
public class LoginEvent extends Event {
    public static EventType<LoginEvent> LoginEventType = new EventType<LoginEvent>("Login Event");

    private String username;
    private String password;

    public LoginEvent(String usern, String pass) {
        super(LoginEventType);

        username = usern;
        password = pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
