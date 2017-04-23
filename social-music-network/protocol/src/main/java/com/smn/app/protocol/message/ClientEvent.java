package com.smn.app.protocol.message;

/**
 * All the events that could happen to the server.
 */
public abstract class ClientEvent {
    public enum Types {
        LOGIN,
        REGISTER,
        VOID
    }

    public ClientEvent.Types type;

    /**
     * Sent by the client when there is an attempt to login or verify the username and password combination.
     */
    public static class Login extends ClientEvent {
        public String username;
        public String password;

        public Login() {
            type = Types.LOGIN;
        }
    }

    /**
     * Sent by the client in an attempt to register a new user.
     */
    public static class Register extends ClientEvent {
        public String username;
        public String password;
        public String name;
        public String email;

        public Register() {
            type = Types.REGISTER;
        }
    }

    /**
     * Represents an incorrect user request, not of any of the standard types.
     */
    public static class Void extends ClientEvent {
        public Void() {
            type = Types.VOID;
        }
    }
}
