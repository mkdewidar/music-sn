package com.smn.app.protocol.message;

import java.util.Map;

/**
 * A response that the server can give the client.
 */
public class ServerEvent {
    public enum Types {
        OK,
        INVALIDAUTH,
        INVALIDREG,
        USERFRIENDS,
        USERSEARCH,
        USERCHANNELS,
        CHANNELMESSAGES,
        INVALIDNEWCHANNEL,
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
     * Represents a failed authentication, the creator and password are not valid.
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
        public String[] requests;

        public UserFriends() {
            type = Types.USERFRIENDS;
        }
    }

    /**
     * The results of a search for another user.
     */
    public static class UserSearch extends ServerEvent {
        public String[] results;

        public UserSearch() {
            type = Types.USERSEARCH;
        }
    }

    /**
     * A list of all the channels the current user is in.
     */
    public static class UserChannels extends ServerEvent {
        public String[] channels;

        public UserChannels() {
            type = Types.USERCHANNELS;
        }
    }

    /**
     * All of the messages in a specific channel.
     */
    public static class ChannelMessages extends ServerEvent {
        // An array of all of the messages, each message is a map of
        // a field (sender, message, date) to the object.
        public Map<String, Object>[] messages;
        // So the client can remember which channel this was for
        // as the UI may have changed in between the transfer.
        public String channelId;

        public ChannelMessages() {
            type = Types.CHANNELMESSAGES;
        }
    }

    /**
     * Sent when the channel couldn't be created.
     */
    public static class InvalidNewChannel extends ServerEvent {
        public InvalidNewChannel() {
            type = Types.INVALIDNEWCHANNEL;
        }
    }
}
