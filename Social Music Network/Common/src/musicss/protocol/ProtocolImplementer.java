package musicss.protocol;

import musicss.protocol.message.Request;
import musicss.protocol.message.Response;

/**
 * Class that implements the application layer protocol.
 * It converts a system message to a message ready to be sent over the network.
 */
public class ProtocolImplementer {
    /**
     * A set of ready static objects that represent certain status'.
     * Useful for saving processing time when checking the status of a response/request.
     */
    public static class StatusCodes {
        // Request was good and has been processed
        public static String OK = "OK";
        // The auth provided was incorrect
        public static String INVALIDAUTH = "invalidAuth";
        // A void reply for a void/incorrect message
        public static String VOID = "void";
    }

    /**
     * Given a packed message, it unpacks it into a Request object to be used by the server.
     * If the message has too few of the required fields (i.e missing data) then a @see musicss.protocol.Response.Void
     *      will be returned by the function.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a Request object.
     */
    public Request unpackRequest(String msg) {
        Request clientRequest = new Request.Void();

        if (msg.startsWith("login:")) {
            Request.Login loginRequest = new Request.Login();

            String[] fields = msg.substring(0, 6).split(",");
            if (fields.length >= 2) {
                loginRequest.username = fields[0];
                loginRequest.password = fields[1];

                clientRequest = loginRequest;
            }
        } else if (msg.startsWith("register:")) {
            Request.Register registerRequest = new Request.Register();

            String[] fields = msg.substring(0, 9).split(",");
            if (fields.length >= 4) {
                registerRequest.name = fields[0];
                registerRequest.username = fields[1];
                registerRequest.password = fields[2];
                registerRequest.email = fields[3];

                clientRequest = registerRequest;
            }
        }

        return clientRequest;
    }

    /**
     * Given a packed message, it unpacks it into a Response object to be parsed by the client.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a Response object.
     */
    public Response unpackResponse(String msg) {
        Response clientRequest = new Response.Void();

        if (msg == StatusCodes.OK) {
            clientRequest = new Response.Ok();
        } else if (msg == StatusCodes.INVALIDAUTH) {
            clientRequest = new Response.InvalidAuth();
        }

        return clientRequest;
    }

    /**
     * Packs a response into the protocol complaint format.
     * @param response The response to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(Response response) {
        String reply = StatusCodes.VOID;

        switch (response.type) {
            case OK:
                reply = StatusCodes.OK;
                break;
            case INVALIDAUTH:
                reply = StatusCodes.INVALIDAUTH;
                break;
        }

        return reply;
    }

    /**
     * Packs a request into the protocol complaint format.
     * @param request The request to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(Request request) {
        String reply = StatusCodes.VOID;

        switch (request.type) {
            case LOGIN:
                Request.Login loginRequest = (Request.Login) request;
                reply = "login:" + loginRequest.username + "," + loginRequest.password;
                break;
            case REGISTER:
                Request.Register registerRequest = (Request.Register) request;
                reply = "register:" + registerRequest.name + "," + registerRequest.username + "," +
                        registerRequest.password + "," + registerRequest.email;
                break;
        }

        return reply;
    }
}
