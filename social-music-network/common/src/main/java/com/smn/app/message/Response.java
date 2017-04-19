package musicss.protocol.message;

/**
 * A response that the server can give the client.
 */
public class Response {
    public enum Types {
        OK,
        INVALIDAUTH,
        VOID
    }

    public Response.Types type;

    /**
     * Sent by the server when the request has been completed and there is no data to return.
     */
    public static class Ok extends Response {
        // if any other data needs to be sent with the confirmation
        public String meta = "";

        public Ok() {
            type = Types.OK;
        }
    }

    /**
     * Represents an incorrect request was sent by the client.
     */
    public static class Void extends Response {
        public String meta = "";

        public Void() {
            type = Types.VOID;
        }
    }

    /**
     * Represents a failed authentication, the username and password are not valid.
     */
    public static class InvalidAuth extends Response {
        public String desc = "";

        public InvalidAuth() {
            type = Types.INVALIDAUTH;
        }
    }
}
