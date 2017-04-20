package com.smn.app.server;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;
import org.bson.Document;

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

    boolean authenticateLogin(String username, String password) {
        Document user = (Document) userInfoCollection.find(Filters.eq("_id", username)).first();
        if (user == null) {
            return false;
        }
        return true;
    }

    boolean registerUser(String username, String password, String name, String email) {
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
}
