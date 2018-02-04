package com.smn.app.protocol.message;

import java.util.Map;

/**
 * All the events that could happen to the server.
 */
public abstract class ClientEvent {
    public enum Types {
        LOGIN,
        REGISTER,
        FRIENDSLIST,
        FRIENDREQUEST,
        USERSEARCH,
        FRIENDREQUESTREPLY,
        CHANNELLIST,
        CREATECHANNEL,
        GETMESSAGES,
        SENDMESSAGE,
        VOID
    }

    public ClientEvent.Types type;

    /**
     * Sent by the client when there is an attempt to login or verify the creator and password combination.
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
     * Sent by the client when a list of the current users friends are needed.
     */
    public static class FriendsList extends ClientEvent {
        public FriendsList() {
            type = Types.FRIENDSLIST;
        }
    }

    /**
     * Signals a client requesting to be friends with another user.
     */
    public static class FriendRequest extends ClientEvent {
        // name of the user to send the request to
        public String username;

        public FriendRequest() {
            type = Types.FRIENDREQUEST;
        }
    }

    /**
     * Signals a reply by the client to a friend request they had.
     */
    public static class FriendRequestReply extends ClientEvent {
        public boolean accept;
        public String sender;

        public FriendRequestReply() {
            type = Types.FRIENDREQUESTREPLY;
        }
    }

    /**
     * Signals a client wanting to search for a user.
     */
    public static class UserSearch extends ClientEvent {
        public String searchString;

        public UserSearch() {
            type = Types.USERSEARCH;
        }
    }

    /**
     * Represents a request to get a list of all the channels the user is in.
     */
    public static class ChannelList extends ClientEvent {
        public ChannelList() {
            type = Types.CHANNELLIST;
        }
    }

    /**
     * Represents a client attempt to create a channel.
     */
    public static class CreateChannel extends ClientEvent {
        public String creator;
        public String channelName;
        public String[] members;

        public CreateChannel() {
            type = Types.CREATECHANNEL;
        }
    }

    /**
     * A request for all the messages for this channel.
     */
    public static class GetMessages extends ClientEvent {
        public String channelId;

        public GetMessages() {
            type = Types.GETMESSAGES;
        }
    }

    /**
     * Signals the client wanting to send a message to a specific channel.
     */
    public static class SendMessage extends ClientEvent {
        public String channelId;
        public Map<String, Object> message;

        public SendMessage() {
            type = Types.SENDMESSAGE;
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
