package com.smn.app.server;

/**
 * Data about the current logged in users.
 * Used as a form of caching for user information.
 */
public class UserServerCookie {
    public String id;
    public String auth;

    public UserServerCookie(String id, String auth) {
        this.id = id;
        this.auth = auth;
    }
}
