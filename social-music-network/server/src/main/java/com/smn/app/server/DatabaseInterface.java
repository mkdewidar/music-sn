package com.smn.app.server;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that allows access to the databases functionality.
 */
public class DatabaseInterface {

    static MongoDatabase mongoDatabase;
    static MongoCollection userInfoCollection;

    public DatabaseInterface() {
        if (mongoDatabase == null) {
            mongoDatabase = new MongoClient().getDatabase("smndb");
            userInfoCollection = mongoDatabase.getCollection("UserInfo");
        }
    }

    /**
     * Given the user login information, returns whether or not it's a valid login.
     * @param username The user's username.
     * @param password The user's password.
     * @return Returns true if the username and password are of a valid user.
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
     * @param username The new user's username.
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
     * @param username The username for the user.
     * @return A String array of all the usernames of the friends, if none, array of size 0.
     */
    public String[] getFriends(String username) {
        Document userInfo = (Document) userInfoCollection.find(Filters.eq("_id", username)).first();
        ArrayList<String> friendList = (ArrayList<String>) userInfo.get("friends");

        if (friendList == null) {
            // Mongo found nothing at that key so no friends
            return null;
        }

        return friendList.toArray(new String[friendList.size()]);
    }

    /**
     * Searches for any user based on their username.
     * @param currentUser The username for the current user, to ignore it in the result.
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
        ArrayList<String> requests = (ArrayList<String>) userInfo.get("requests");

        if (requests == null) {
            // Mongo found nothing at that key so no friend requests
            return null;
        }

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
     * @return Returns the new list of friends.
     */
    public String[] acceptFriendRequest(String user, String sender) {
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.addToSet("friends", sender));
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.pull("requests", sender));
        userInfoCollection.updateOne(Filters.eq("_id", sender), Updates.addToSet("friends", user));

        return getFriends(user);
    }

    /**
     * Remove a friend request by sender to user.
     * @param user The current user.
     * @param sender The person sending the request to be rejected.
     */
    public void rejectFriendRequest(String user, String sender) {
        userInfoCollection.updateOne(Filters.eq("_id", user), Updates.pull("requests", sender));
    }
}
