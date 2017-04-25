package com.smn.app.server;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * The class that allows access to the databases functionality.
 */
public class DatabaseInterface {

    static MongoDatabase mongoDatabase;
    static MongoCollection userInfoCollection;
    static MongoCollection channelCollection;

    public DatabaseInterface() {
        if (mongoDatabase == null) {
            mongoDatabase = new MongoClient().getDatabase("smndb");
            userInfoCollection = mongoDatabase.getCollection("UserInfo");
            channelCollection = mongoDatabase.getCollection("Channels");
        }
    }

    /**
     * Given the user login information, returns whether or not it's a valid login.
     * @param username The user's creator.
     * @param password The user's password.
     * @return Returns true if the creator and password are of a valid user.
     */
    public boolean authenticateLogin(String username, String password) {
        Document user = (Document) userInfoCollection.find(Filters.eq("_id", username)).first();
        if (user == null) {
            return false;
        }
        return true;
    }

    /**
     * Given new user information, determines whether or not the registration was successful.
     * @param username The new user's creator.
     * @param password The new user's password.
     * @param name The new user's name.
     * @param email The new user's email.
     * @return Returns true if the user registered successfully.
     */
    public boolean registerUser(String username, String password, String name, String email) {
        Document newUser = new Document().append("_id", username)
                .append("password", password).append("name", name).append("email", email)
                .append("channels", new ArrayList<String>())
                .append("friends", new ArrayList<String>())
                .append("requests", new ArrayList<String>());

        try {
            userInfoCollection.insertOne(newUser);
        } catch (MongoException e) {
            System.err.println("ERROR: failed to register user\n\t" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Gets all of the current users friends.
     * @param username The creator for the user.
     * @return A String array of all the usernames of the friends, if none, array of size 0.
     */
    public String[] getFriends(String username) {
        Document userInfo = (Document) userInfoCollection.find(Filters.eq("_id", username)).first();

        // Mongo will return an array of size 0 if empty
        ArrayList<String> friendList = (ArrayList<String>) userInfo.get("friends");

        return friendList.toArray(new String[friendList.size()]);
    }

    /**
     * Searches for any user based on their creator.
     * @param currentUser The creator for the current user, to ignore it in the result.
     * @param searchString The regex search string to search the usernames by.
     * @return A String array of all of the users matching the search string, if none, array of size 0.
     */
    public String[] searchUsers(String currentUser, String searchString) {
        MongoCursor<Document> resultCursor = userInfoCollection.find(Filters.regex("_id", searchString)).iterator();
        ArrayList<String> results = new ArrayList<>();

        while (resultCursor.hasNext()) {
            String nextUser = resultCursor.next().getString("_id");
            if (!nextUser.equals(currentUser)) {
                results.add(nextUser);
            }
        }

        return results.toArray(new String[results.size()]);
    }

    public String[] getFriendRequests(String username) {
        Document userInfo = (Document) userInfoCollection.find(Filters.eq("_id", username)).first();

        // Mongo will return an array of size 0 if empty
        ArrayList<String> requests = (ArrayList<String>) userInfo.get("requests");

        return requests.toArray(new String[requests.size()]);
    }

    /**
     * Adds a friend request to receiver's document from sender.
     * @param sender The sender of the friend request.
     * @param receiver The receiver of the friend request.
     */
    public void sendFriendRequest(String sender, String receiver) {
        userInfoCollection.updateOne(Filters.eq("_id", receiver), Updates.addToSet("requests", sender));
    }

    /**
     * Accept a friend request in user made by sender.
     * @param user The current user.
     * @param sender The one who sent the friend request.
     */
    public void acceptFriendRequest(String user, String sender) {
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.addToSet("friends", sender));
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.pull("requests", sender));
        userInfoCollection.updateOne(Filters.eq("_id", sender), Updates.addToSet("friends", user));
    }

    /**
     * Remove a friend request by sender to user.
     * @param user The current user.
     * @param sender The person sending the request to be rejected.
     */
    public void rejectFriendRequest(String user, String sender) {
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.pull("requests", sender));
    }

    /**
     * Creates a new channel for this specific user given the information provided.
     * @param username The name of the user creating the channel.
     * @param channelName The name of the new channel to be made.
     * @param members The list of users which are members of this channel.
     * @return True if the channel was created successfully.
     */
    public boolean createChannel(String username, String channelName, String[] members) {
        String channelId = username + "_" + channelName;
        Document newChannel = new Document().append("_id", channelId).append("members", Arrays.asList(members))
                .append("creation-date", new Date()).append("messages", new ArrayList<BasicDBObject>())
                .append("attachments", new ArrayList<BasicDBObject>());

        try {
            channelCollection.insertOne(newChannel);
        } catch (MongoException e) {
            System.err.println("ERROR: A channel for this user already exists under this name\n\t" + e.getMessage());
            return false;
        }

        // Add the channel to the list of channels for all of the members.
        userInfoCollection.updateOne(Filters.eq("_id", username), Updates.addToSet("channels", channelId));
        for (String member : members) {
            try {
                userInfoCollection.updateOne(Filters.eq("_id", member), Updates.addToSet("channels", channelId));
            } catch (MongoException e) {
                System.err.println("ERROR: This is not a valid user, skipping\n\t" + e.getMessage());
            }
        }

        return true;
    }

    /**
     * Gets all of the channels that belong to this user.
     * @param currentUser The name of the current user.
     * @return An array of all of the channels that the user has.
     */
    public String[] getChannels(String currentUser) {
        Document userInfo = (Document) userInfoCollection.find(Filters.eq("_id", currentUser)).first();

        // Mongo will return an array of size 0 if empty
        ArrayList<String> channels = (ArrayList<String>) userInfo.get("channels");

        return channels.toArray(new String[channels.size()]);
    }

    /**
     * Gets all the messages in the provided channel.
     * @param channelId The id of the channel.
     * @return An array of maps of strings to objects, each map being a message, maps string to object.
     */
    public Map<String, Object>[] getMessages(String channelId) {
        Document channel = (Document) channelCollection.find(Filters.eq("_id", channelId)).first();
        ArrayList<BasicDBObject> messagesList = (ArrayList<BasicDBObject>) channel.get("messages");

        Map<String, Object>[] messagesMap = new Map[messagesList.size()];

        for (int msgIndex = 0; msgIndex < messagesList.size(); msgIndex++) {
            messagesMap[msgIndex] = messagesList.get(msgIndex).toMap();
        }

        return messagesMap;
    }

    /**
     * Adds a message to the channel.
     * @param sender The name of the user sending the message.
     * @param channelId The id of the channel the message is to be added to.
     * @param msg The message the user is sending.
     * @param timestamp The date the message got sent.
     */
    public void addMessage(String sender, String channelId, String msg, Date timestamp) {
        Document msgDocument = new Document().append("sender", sender).append("contents", msg)
                .append("timestamp", timestamp);

        channelCollection.updateOne(Filters.eq("_id", channelId), Updates.addToSet("messages", msgDocument));
    }
}
