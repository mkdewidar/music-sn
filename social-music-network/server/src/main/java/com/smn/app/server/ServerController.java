package com.smn.app.server;

import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

import java.util.Date;
import java.util.HashMap;

/**
 * The class that executes the requests and deals with the server logic.
 */
public class ServerController {

    // All the server cookies for all currently logged in users.
    // Maps the creator to the cookie
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
                break;
            }

            case CREATECHANNEL: {
                ClientEvent.CreateChannel createChannel = (ClientEvent.CreateChannel) clientEvent;

                if (database.createChannel(createChannel.creator, createChannel.channelName, createChannel.members)) {
                    ServerEvent.UserChannels channelList = new ServerEvent.UserChannels();
                    channelList.channels = database.getChannels(userServerCookie.id);
                    serverEvent = channelList;
                } else {
                    serverEvent = new ServerEvent.InvalidNewChannel();
                }

                break;
            }

            case GETMESSAGES: {
                ClientEvent.GetMessages getMessages = (ClientEvent.GetMessages) clientEvent;

                ServerEvent.ChannelMessages channelMessages = new ServerEvent.ChannelMessages();
                channelMessages.messages = database.getMessages(getMessages.channelId);
                channelMessages.channelId = getMessages.channelId;

                serverEvent = channelMessages;
                break;
            }

            case CHANNELLIST: {
                ServerEvent.UserChannels userChannels = new ServerEvent.UserChannels();

                userChannels.channels = database.getChannels(userServerCookie.id);

                serverEvent = userChannels;
                break;
            }

            case SENDMESSAGE: {
                ClientEvent.SendMessage sendMessage = (ClientEvent.SendMessage) clientEvent;

                database.addMessage((String) sendMessage.message.get("sender"), sendMessage.channelId,
                        (String) sendMessage.message.get("message"), (Date) sendMessage.message.get("timestamp"));

                serverEvent = new ServerEvent.Ok();

                break;
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
