package com.smn.app.client.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Custom events that are fired by UI components to notify the stage of change in scenes.
 */
public class AppEvent extends Event {
    public static EventType<AppEvent> Type = new EventType<>(Event.ANY, "AppEvent.ANY");

    public AppEvent() {
        super(Type);
    }


    /**
     * An event representing a successful login and a change to the application scene.
     */
    public static class Login extends Event {
        public static EventType<Login> Type = new EventType<>(AppEvent.ANY, "AppEvent.Login");

        public String username;
        public String password;

        public Login() {
            super(Type);
        }
    }

    /**
     * An event representing a logout and head back to the login screen.
     */
    public static class Logout extends Event {
        public static EventType<Logout> Type = new EventType<>(AppEvent.ANY, "AppEvent.Logout");

        public Logout() {
            super(Type);
        }
    }
}
