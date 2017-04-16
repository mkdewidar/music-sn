package musicss.server;

import musicss.protocol.message.Request;
import musicss.protocol.message.Response;

/**
 * The class that executes the requests and deals with the server logic.
 */
public class Executor {

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
                    response = new Response.Ok();
                else
                    response = new Response.InvalidAuth();
                // TODO: timestamp the user login and register their IP
                break;
            case REGISTER:
                // TODO: register the user
                break;
        }

        return response;
    }
}
