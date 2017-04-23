package com.smn.app.protocol.message;

/**
 * A response that the server can give the client.
 */
public class ServerEvent {
    public enum Types {
        OK,
        INVALIDAUTH,
        INVALIDREG,
        USERFRIENDS,
        FRIENDSEARCH,
        VOID
    }

    public ServerEvent.Types type;

    /**
     * Sent by the server when the request has been completed and there is no data to return.
     */
    public static class Ok extends ServerEvent {
        // if any other data needs to be sent with the confirmation
        public String meta = "";

        public Ok() {
            type = Types.OK;
        }
    }

    /**
     * Represents an incorrect request was sent by the client.
     */
    public static class Void extends ServerEvent {
        public String meta = "";

        public Void() {
            type = Types.VOID;
        }
    }

    /**
     * Represents a failed authentication, the username and password are not valid.
     */
    public static class InvalidAuth extends ServerEvent {
        public String desc = "";

        public InvalidAuth() {
            type = Types.INVALIDAUTH;
        }
    }

    /**
     * Represents a failed user registration, one of the fields is not valid.
     */
    public static class InvalidReg extends ServerEvent {
        public String desc = "";

        public InvalidReg() {
            type = Types.INVALIDREG;
        }
    }

    /**
     * An event carrying the list of the current user's friends.
     */
    public static class UserFriends extends ServerEvent {
        public String[] friends;

        public UserFriends() {
            type = Types.USERFRIENDS;
        }
    }

    /**
     * The results of a search for another user.
     */
    public static class FriendSearch extends ServerEvent {
        public String[] results;

        public FriendSearch() {
            type = Types.FRIENDSEARCH;
        }
    }
}
