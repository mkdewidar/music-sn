package musicss.protocol.message;

/**
 * A response that the server can give the client.
 */
public class Response {
    public enum Types {
        OK,
        INVALIDAUTH
    }

    public Response.Types type;

    public static class Ok extends Response {
        // if any other data needs to be sent with the confirmation
        public String meta = "";

        public Ok() {
            type = Types.OK;
        }
    }

    public static class InvalidAuth extends Response {
        public String desc = "";

        public InvalidAuth() {
            type = Types.INVALIDAUTH;
        }
    }
}
