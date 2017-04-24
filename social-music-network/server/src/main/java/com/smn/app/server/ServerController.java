package com.smn.app.server;

import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

import java.util.HashMap;

/**
 * The class that executes the requests and deals with the server logic.
 */
public class ServerController {

    // All the server cookies for all currently logged in users.
    // Maps the username to the cookie
    public static HashMap<String, UserServerCookie> serverCookies;
    public DatabaseInterface database;

    private UserServerCookie userServerCookie;

    public ServerController() {
        database = new DatabaseInterface();
    }

    /**
     * Processes the clientEvent object provided.
     * @param clientEvent The clientEvent object that we got from the client.
     * @return The response that should be sent to the client.
     */
    public ServerEvent process(ClientEvent clientEvent) {
        ServerEvent serverEvent = new ServerEvent.Void();

        switch (clientEvent.type) {
            case LOGIN: {
                ClientEvent.Login loginRequest = (ClientEvent.Login) clientEvent;
                if (database.authenticateLogin(loginRequest.username, loginRequest.password)) {
                    serverEvent = new ServerEvent.Ok();

                    Login(loginRequest.username, loginRequest.password);
                } else {
                    serverEvent = new ServerEvent.InvalidAuth();
                }
                break;
            }
            case REGISTER: {
                ClientEvent.Register registerRequest = (ClientEvent.Register) clientEvent;
                if (database.registerUser(registerRequest.username, registerRequest.password,
                        registerRequest.name, registerRequest.email)) {
                    serverEvent = new ServerEvent.Ok();

                    Login(registerRequest.username, registerRequest.password);
                } else {
                    serverEvent = new ServerEvent.InvalidReg();
                }
                break;
            }
            case FRIENDSLIST: {
                ServerEvent.UserFriends userFriends = new ServerEvent.UserFriends();

                userFriends.friends = database.getFriends(userServerCookie.id);
                userFriends.requests = database.getFriendRequests(userServerCookie.id);

                serverEvent = userFriends;
                break;

            }
            case USERSEARCH: {
                ClientEvent.UserSearch userSearch = (ClientEvent.UserSearch) clientEvent;

                ServerEvent.UserSearch friendSearch = new ServerEvent.UserSearch();
                friendSearch.results = database.searchUsers(userServerCookie.id, userSearch.searchString);

                serverEvent = friendSearch;
                break;
            }
            case FRIENDREQUEST: {
                ClientEvent.FriendRequest friendRequest = (ClientEvent.FriendRequest) clientEvent;

                database.sendFriendRequest(userServerCookie.id, friendRequest.username);
                // TODO: notify the other user of the friend request

                serverEvent = new ServerEvent.Ok();
                break;
            }
            case FRIENDREQUESTREPLY: {
                ClientEvent.FriendRequestReply friendRequestReply = (ClientEvent.FriendRequestReply) clientEvent;
                ServerEvent.UserFriends userFriends = new ServerEvent.UserFriends();

                if (friendRequestReply.accept) {

                    database.acceptFriendRequest(userServerCookie.id, friendRequestReply.sender);
                    //TODO: send an event to the sender telling them of the accepted request

                } else {
                    database.rejectFriendRequest(userServerCookie.id, friendRequestReply.sender);
                }

                userFriends.friends = database.getFriends(userServerCookie.id);
                userFriends.requests = database.getFriendRequests(userServerCookie.id);

                serverEvent = userFriends;
            }
        }

        return serverEvent;
    }

    /**
     * Log a user into the system, also called once user registration is complete.
     * @param username The id of the user.
     * @param password The authentication token of the user.
     */
    private void Login(String username, String password) {
        userServerCookie = new UserServerCookie(username, password);
        // TODO: timestamp the user login and register their IP
    }
}
