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
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a Request object.
     */
    public Request unpack(String msg) {
        Request clientRequest = new Request.Void();

        if (msg.startsWith("login:")) {
            Request.Login loginRequest = new Request.Login();

            String[] fields = msg.substring(0, 6).split(",");
            loginRequest.username = fields[0];
            loginRequest.password = fields[1];

            clientRequest = loginRequest;
        } else if (msg.startsWith("register:")) {
            Request.Register registerRequest = new Request.Register();

            String[] fields = msg.substring(0, 9).split(",");
            registerRequest.name = fields[0];
            registerRequest.username = fields[1];
            registerRequest.password = fields[2];
            registerRequest.email = fields[3];

            clientRequest = registerRequest;
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
}
