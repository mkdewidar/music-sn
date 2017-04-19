package musicss.server;

import musicss.protocol.message.Request;
import musicss.protocol.message.Response;

import java.util.HashMap;

/**
 * The class that executes the requests and deals with the server logic.
 */
public class ServerController {

    // All the server cookies for all currently logged in users.
    public static HashMap<String, UserServerCookie> serverCookies;
    public static DatabaseInterface database;

    private UserServerCookie userServerCookie;

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
                if (loginRequest.username.contains("asd") && loginRequest.password.contains("asd"))
                {
                    response = new Response.Ok();

                    userServerCookie.id = loginRequest.username;
                    userServerCookie.auth = loginRequest.password;
                    // TODO: timestamp the user login and register their IP
                }
                else
                {
                    response = new Response.InvalidAuth();
                }
                break;
            case REGISTER:
                // TODO: register the user
                break;
        }

        return response;
    }
}
