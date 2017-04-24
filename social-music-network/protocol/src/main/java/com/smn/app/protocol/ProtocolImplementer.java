package com.smn.app.protocol;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

/**
 * Class that implements the application layer protocol.
 * It converts a system event to a message ready to be sent over the network.
 */
public class ProtocolImplementer {
    /**
     * A set of ready static objects that represent certain status'.
     * Useful for saving processing time when checking the status of an event.
     */
    public static class StatusCodes {
        // ClientEvent was good and has been processed
        public static final String OK = "OK";
        // The auth provided was incorrect
        public static final String INVALIDAUTH = "invalidAuth";
        // The registration information is incorrect
        public static final String INVALIDREG = "invalidReg";
        // A void reply for a void/incorrect protocol
        public static final String VOID = "void";
    }

    /**
     * Given a packed message, it unpacks it into a ClientEvent object to be used by the server.
     * If the message has too few of the required fields (i.e missing data) then a @see musicss.protocol.ServerEvent.Void
     *      will be returned by the function.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a ClientEvent object.
     */
    public ClientEvent unpackClientEvent(String msg) {
        // If it's none of these messages, it's a void message
        ClientEvent clientEvent = new ClientEvent.Void();
        // The message converted from JSON to BSON
        BSONObject bMsg = (BSONObject) JSON.parse(msg);

        String msgType = (String) bMsg.get("type");

        switch (msgType) {
            case "login":
                ClientEvent.Login loginRequest = new ClientEvent.Login();

                loginRequest.username = (String) bMsg.get("username");
                loginRequest.password = (String) bMsg.get("password");

                clientEvent = loginRequest;
                break;

            case "register":
                ClientEvent.Register registerRequest = new ClientEvent.Register();

                registerRequest.name = (String) bMsg.get("name");
                registerRequest.username = (String) bMsg.get("username");
                registerRequest.password = (String) bMsg.get("password");
                registerRequest.email = (String) bMsg.get("email");

                clientEvent = registerRequest;
                break;

            case "get-friends":
                ClientEvent.FriendsList friendsRequest = new ClientEvent.FriendsList();

                clientEvent = friendsRequest;
                break;

            case "friend-request":
                ClientEvent.FriendRequest friendRequest = new ClientEvent.FriendRequest();

                friendRequest.username = (String) bMsg.get("receiver");

                clientEvent = friendRequest;
                break;

            case "user-search":
                ClientEvent.UserSearch userSearch = new ClientEvent.UserSearch();

                userSearch.searchString = (String) bMsg.get("search-string");

                clientEvent = userSearch;
                break;
        }

        return clientEvent;
    }

    /**
     * Given a packed message, it unpacks it into a ServerEvent object to be parsed by the client.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a ServerEvent object.
     */
    public ServerEvent unpackServerEvent(String msg) {
        ServerEvent serverEvent = new ServerEvent.Void();
        BSONObject bMsg = (BSONObject) JSON.parse(msg);

        String msgType = (String) bMsg.get("type");

        switch (msgType) {
            case StatusCodes.OK:
                serverEvent = new ServerEvent.Ok();
                break;

            case StatusCodes.INVALIDAUTH:
                serverEvent = new ServerEvent.InvalidAuth();
                break;

            case StatusCodes.INVALIDREG:
                serverEvent = new ServerEvent.InvalidReg();
                break;

            case "get-friends":
                ServerEvent.UserFriends friendListEvent = new ServerEvent.UserFriends();

                BasicDBList friends = (BasicDBList) bMsg.get("friends");
                if (friends == null) {
                    friendListEvent.friends = null;
                } else {
                    friendListEvent.friends = new String[friends.size()];
                    friends.toArray(friendListEvent.friends);
                }

                BasicDBList requests = (BasicDBList) bMsg.get("requests");
                if (requests == null) {
                    friendListEvent.requests = null;
                } else {
                    friendListEvent.requests = new String[requests.size()];
                    requests.toArray(friendListEvent.requests);
                }

                serverEvent = friendListEvent;
                break;

            case "user-search":
                ServerEvent.UserSearch userSearch = new ServerEvent.UserSearch();

                BasicDBList results = (BasicDBList) bMsg.get("results");
                if (results == null) {
                    userSearch.results = null;
                } else {
                    userSearch.results = new String[results.size()];
                    results.toArray(userSearch.results);
                }

                serverEvent = userSearch;
                break;
        }

        return serverEvent;
    }

    /**
     * Packs a serverEvent into the protocol complaint format.
     * @param serverEvent The serverEvent to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(ServerEvent serverEvent) {
        BSONObject bMsg = new BasicBSONObject();
        bMsg.put("type", StatusCodes.VOID);

        switch (serverEvent.type) {
            case OK:
                bMsg.put("type", StatusCodes.OK);
                break;

            case INVALIDAUTH:
                bMsg.put("type", StatusCodes.INVALIDAUTH);
                break;

            case INVALIDREG:
                bMsg.put("type", StatusCodes.INVALIDREG);
                break;

            case USERFRIENDS:
                ServerEvent.UserFriends friendsListEvent = (ServerEvent.UserFriends) serverEvent;

                bMsg.put("type", "get-friends");
                if (friendsListEvent.friends != null) {
                    bMsg.put("friends", friendsListEvent.friends);
                }
                if (friendsListEvent.friends != null) {
                    bMsg.put("requests", friendsListEvent.requests);
                }

                break;

            case USERSEARCH:
                ServerEvent.UserSearch userSearch = (ServerEvent.UserSearch) serverEvent;

                bMsg.put("type", "user-search");
                if (userSearch.results != null) {
                    bMsg.put("results", userSearch.results);
                }

                break;
        }

        return JSON.serialize(bMsg);
    }

    /**
     * Packs a clientEvent into the protocol complaint format.
     * @param clientEvent The clientEvent to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(ClientEvent clientEvent) {
        BSONObject bMsg = new BasicBSONObject();
        bMsg.put("type", StatusCodes.VOID);

        switch (clientEvent.type) {
            case LOGIN:
                ClientEvent.Login login = (ClientEvent.Login) clientEvent;

                bMsg.put("type", "login");

                bMsg.put("username", login.username);
                bMsg.put("password", login.password);

                break;
            case REGISTER:
                ClientEvent.Register register = (ClientEvent.Register) clientEvent;

                bMsg.put("type", "register");

                bMsg.put("username", register.username);
                bMsg.put("password", register.password);
                bMsg.put("name", register.name);
                bMsg.put("email", register.email);

                break;
            case FRIENDSLIST:
                bMsg.put("type", "get-friends");
                break;
            case FRIENDREQUEST:
                ClientEvent.FriendRequest friendRequest = (ClientEvent.FriendRequest) clientEvent;

                bMsg.put("type", "friend-request");

                bMsg.put("receiver", friendRequest.username);

                break;
            case USERSEARCH:
                ClientEvent.UserSearch userSearch = (ClientEvent.UserSearch) clientEvent;

                bMsg.put("type", "user-search");

                bMsg.put("search-string", userSearch.searchString);

                break;
        }

        return JSON.serialize(bMsg);
    }
}
