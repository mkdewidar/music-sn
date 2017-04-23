package com.smn.app.server;

import com.smn.app.protocol.message.Request;
import com.smn.app.protocol.message.Response;

import java.util.HashMap;

/**
 * The class that executes the requests and deals with the server logic.
 */
public class ServerController {

    // All the server cookies for all currently logged in users.
    public static HashMap<String, UserServerCookie> serverCookies;
    public DatabaseInterface database;

    private UserServerCookie userServerCookie;

    public ServerController() {
        database = new DatabaseInterface();
    }

    /**
     * Processes the request object provided.
     * @param request The request object that we got from the client.
     * @return The response that should be sent to the client.
     */
    public Response process(Request request) {
        Response response = new Response.Void();

        switch (request.type) {
            case LOGIN:
                Request.Login loginRequest = (Request.Login)request;
                if (database.authenticateLogin(loginRequest.username, loginRequest.password)) {
                    response = new Response.Ok();

                    Login(loginRequest.username, loginRequest.password);
                } else {
                    response = new Response.InvalidAuth();
                }
                break;
            case REGISTER:
                Request.Register registerRequest = (Request.Register)request;
                if (database.registerUser(registerRequest.username, registerRequest.password,
                        registerRequest.name, registerRequest.email)) {
                    response = new Response.Ok();

                    Login(registerRequest.username, registerRequest.password);
                } else {
                    response = new Response.InvalidReg();
                }
                break;
        }

        return response;
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
