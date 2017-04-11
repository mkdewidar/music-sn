package musicss.server.message;

/**
 * All the events that could happen to the server.
 */
public abstract class Request {
    public enum Types {
        LOGIN,
        REGISTER,
        VOID,
        LISTUSERS
    }

    public Request.Types type;

    public abstract Response execute();

    public static class Login extends Request {
        public String username;
        public String password;

        public Login() {
            type = Types.LOGIN;
        }

        @Override
        public Response execute() {
            // TODO: Verify client login information
            // TODO: Timestamp the user login and device ip

            System.out.println("The user " + username + " has logged in");

            return new Response.Ok();
        }
    }

    public static class Register extends Request {
        public String username;
        public String password;
        public String name;
        public String email;

        public Register() {
            type = Types.REGISTER;
        }

        @Override
        public Response execute() {
            // TODO: Verify username is unique
            // TODO: Verify email has not been used
            // TODO: Create user account
            return new Response.Ok();
        }
    }

    public static class Void extends Request {
        public Void() {
            type = Types.VOID;
        }

        @Override
        public Response execute() {
            System.out.println("User sent an invalid or useless packet");

            return new Response.Ok();
        }
    }
}
