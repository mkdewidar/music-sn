package com.smn.app.server;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
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
                .append("channels", null).append("friends", null).append("requests", null);

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
        Object friendList = userInfo.get("friends");

        if (friendList == null) {
            // Mongo found nothing at that key so no friends
            return null;
        }

        return (String[]) ((List<String>) friendList).toArray();
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
}
