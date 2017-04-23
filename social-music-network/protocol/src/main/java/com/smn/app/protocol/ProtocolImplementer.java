package com.smn.app.protocol;

import com.smn.app.protocol.message.ClientEvent;
import com.smn.app.protocol.message.ServerEvent;

/**
 * Class that implements the application layer protocol.
 * It converts a system event to a message ready to be sent over the network.
 */
public class ProtocolImplementer {
    /**
     * A set of ready static objects that represent certain status'.
     * Useful for saving processing time when checking the status of an event.
     */
    public static class StatusCodes {
        // ClientEvent was good and has been processed
        public static String OK = "OK";
        // The auth provided was incorrect
        public static String INVALIDAUTH = "invalidAuth";
        // The registration information is incorrect
        public static String INVALIDREG = "invalidReg";
        // A void reply for a void/incorrect protocol
        public static String VOID = "void";
    }

    /**
     * Given a packed message, it unpacks it into a ClientEvent object to be used by the server.
     * If the message has too few of the required fields (i.e missing data) then a @see musicss.protocol.ServerEvent.Void
     *      will be returned by the function.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a ClientEvent object.
     */
    public ClientEvent unpackClientEvent(String msg) {
        ClientEvent clientClientEvent = new ClientEvent.Void();

        if (msg.startsWith("login:")) {
            ClientEvent.Login loginRequest = new ClientEvent.Login();

            String[] fields = msg.substring(6).split(",");
            if (fields.length >= 2) {
                loginRequest.username = fields[0];
                loginRequest.password = fields[1];

                clientClientEvent = loginRequest;
            }
        } else if (msg.startsWith("register:")) {
            ClientEvent.Register registerRequest = new ClientEvent.Register();

            String[] fields = msg.substring(9).split(",");
            if (fields.length >= 4) {
                registerRequest.name = fields[0];
                registerRequest.username = fields[1];
                registerRequest.password = fields[2];
                registerRequest.email = fields[3];

                clientClientEvent = registerRequest;
            }
        }

        return clientClientEvent;
    }

    /**
     * Given a packed message, it unpacks it into a ServerEvent object to be parsed by the client.
     * @param msg The packed message to be unpacked.
     * @return The unpacked message as a ServerEvent object.
     */
    public ServerEvent unpackServerEvent(String msg) {
        ServerEvent serverEvent = new ServerEvent.Void();

        if (msg.equals(StatusCodes.OK)) {
            serverEvent = new ServerEvent.Ok();
        } else if (msg.equals(StatusCodes.INVALIDAUTH)) {
            serverEvent = new ServerEvent.InvalidAuth();
        } else if (msg.equals(StatusCodes.INVALIDREG)) {
            serverEvent = new ServerEvent.InvalidReg();
        }

        return serverEvent;
    }

    /**
     * Packs a serverEvent into the protocol complaint format.
     * @param serverEvent The serverEvent to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(ServerEvent serverEvent) {
        String reply = StatusCodes.VOID;

        switch (serverEvent.type) {
            case OK:
                reply = StatusCodes.OK;
                break;
            case INVALIDAUTH:
                reply = StatusCodes.INVALIDAUTH;
                break;
            case INVALIDREG:
                reply = StatusCodes.INVALIDREG;
                break;
        }

        return reply;
    }

    /**
     * Packs a clientEvent into the protocol complaint format.
     * @param clientEvent The clientEvent to be packed.
     * @return The packed message that is ready to be sent.
     */
    public String pack(ClientEvent clientEvent) {
        String reply = StatusCodes.VOID;

        switch (clientEvent.type) {
            case LOGIN:
                ClientEvent.Login loginRequest = (ClientEvent.Login) clientEvent;
                reply = "login:" + loginRequest.username + "," + loginRequest.password;
                break;
            case REGISTER:
                ClientEvent.Register registerRequest = (ClientEvent.Register) clientEvent;
                reply = "register:" + registerRequest.name + "," + registerRequest.username + "," +
                        registerRequest.password + "," + registerRequest.email;
                break;
        }

        return reply;
    }
}
