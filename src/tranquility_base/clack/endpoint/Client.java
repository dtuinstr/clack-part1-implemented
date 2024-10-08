package tranquility_base.clack.endpoint;

import tranquility_base.clack.message.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    /**
     * Default port for connecting to server. This should be
     * a port listed as "unassigned" in
     * <a href="https://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.txt">IANA</a>.
     */
    public static final int DEFAULT_SERVER_PORT = 4466;

    /**
     * The server to connect to if one is not explicitly given.
     */
    public static final String DEFAULT_SERVER_NAME = "localhost";

    private final String prompt;
    private final String serverName;
    private final int serverPort;
    private final String username;
    private final Scanner inputScanner = new Scanner(System.in);

    private Message messageToSend;
    private Message messageReceived;

    /**
     * Full constructor, allowing specification of username,
     * server name, and server port.
     *
     * @param username username to include in all messages.
     * @param serverName name of server to connect to at start.
     * @param serverPort port on that server.
     */
    public Client(String username, String serverName, int serverPort) {
        this.username = username;
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.prompt = serverName + "> ";
    }

    /**
     * Constructor using default port. When client starts, it
     * will connect to the default port on the given server.
     *
     * @param username username to include in all messages.
     * @param serverName name of server to connect to at start.
     */
    public Client(String username, String serverName) {
        this(username, serverName, DEFAULT_SERVER_PORT);
    }

    /**
     * Constructor using default server. When client starts,
     * it will connect to the given port on the default server.
     *
     * @param username username to include in all messages.
     * @param serverPort port on the default server to connect to.
     */
    public Client(String username, int serverPort) {
        this(username, DEFAULT_SERVER_NAME, serverPort);
    }

    /**
     * Constructor using default server and port. When client starts,
     * it will connect to the default port on the default server.
     *
     * @param username username to include in all messages.
     */
    public Client(String username) {
        this(username, DEFAULT_SERVER_NAME, DEFAULT_SERVER_PORT);
    }

    /**
     * The client's REPL loop. Prompt for input, build
     * message from it, send message and receive/process
     * the reply, print info for user; repeat until
     * user enters "LOGOUT".
     */
    public void start() {
        do {
            messageToSend = readUserInput();

            if (messageToSend.getMsgType() == Message.MSGTYPE_HELP) {
                System.out.println(messageToSend.getData()[0]);
                continue;
            }
            // At this point we have a valid (non-help) message.

            // HERE IS WHERE WE SEND IT.
            // Careful -- this creates two references to same object.
            messageReceived = messageToSend;

            // HERE IS WHERE WE PROCESS THE RESPONSE.
            String[] data = messageReceived.getData();
            switch (messageReceived.getMsgType()) {
                case Message.MSGTYPE_FILE:
                    FileMessage fileMessage = (FileMessage) messageReceived;
                    System.out.println("Writing file " + data[1] + " ...");
                    try {
                        fileMessage.writeFile();
                        System.out.println("File written.");
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not write file " + data[1] + ". " + e);
                    }
                    break;
                case Message.MSGTYPE_LOGOUT:
                    System.out.println("Logged out.");
                    break;
                case Message.MSGTYPE_LISTUSERS:
                    System.out.println("In production this will be a users list.");
                    break;
                case Message.MSGTYPE_TEXT:
                    System.out.println(data[0]);
                    break;
                default:
                    System.out.println("PROGRAM ERROR. NOTIFY DEVELOPERS.");
            }

            // TESTING OUTPUT. COMMENT OUT FOR PRODUCTION.
            System.out.println("data received    : " + Arrays.toString(data));
            System.out.println("message received : " + messageReceived);
            System.out.println("received getClass: " + messageReceived.getClass());

        } while (messageToSend.getMsgType() != Message.MSGTYPE_LOGOUT);
    }

    /**
     * Parse a line of user input and create the appropriate
     * message.
     *
     * @return an object of the appropriate Message subclass.
     */
    public Message readUserInput() {
        String input;           // what the user actually enters
        String trimmedInput;    // trimmed user input

        // loop until user enters something substantial.
        do {
            System.out.print(prompt);
            input = inputScanner.nextLine();
            trimmedInput = input.trim();
        } while (trimmedInput.isEmpty());

        String[] tokens = trimmedInput.split("\\s+");

        switch (tokens[0].toUpperCase()) {
            case "HELP" :
                return new HelpMessage(username);
            case "LIST" :
                if (tokens[1].equalsIgnoreCase("USERS")) {
                    return new ListUsersMessage(username);
                } else {
                    return new TextMessage(username, input);
                }
            case "LOGOUT" :
                return new LogoutMessage(username);
            case "SEND" :
                if (tokens.length == 1 || !tokens[1].equalsIgnoreCase("FILE")) {
                    return new TextMessage(username, input);
                } else {
                    // We have SEND FILE ...
                    switch (tokens.length) {
                        case 2: // only "SEND FILE" found
                            return new HelpMessage(username, "Invalid SEND FILE syntax.");
                        case 3: // "SEND FILE filepath"
                            try {
                                FileMessage msg = new FileMessage(username, tokens[2]);
                                msg.readFile();
                                return msg;
                            } catch (IOException e) {
                                return new HelpMessage(username, e.getMessage());
                            }
                        case 4: // SEND FILE token2 token3
                            return new HelpMessage(username, "Invalid SEND FILE syntax.");
                        case 5: // SEND FILE token2 token3 token4
                            if (tokens[3].equalsIgnoreCase("AS")) {
                                try {
                                    FileMessage msg = new FileMessage(username, tokens[2], tokens[4]);
                                    msg.readFile();
                                    return msg;
                                } catch (IOException e) {
                                    return new HelpMessage(username, e.getMessage());
                                }
                            } else {
                                return new HelpMessage(username, "Invalid SEND FILE syntax.");
                            }
                        default: // SEND FILE token2 token3 token4 token5 ...
                            return new HelpMessage(username, "Invalid SEND FILE syntax.");
                    }
                }
                // end case "SEND"
            default:    // input does not start with keyword
                return new TextMessage(username, input);
        }
    }

    /**
     * Print the current messageReceived object to System.out.
     * What is printed is the result of calling toString()
     * on the messageReceived object.
     */
    public void printMessage() {
        System.out.println(messageReceived);
    }

    /**
     * Return the username given when client was started.
     *
     *  @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the serverName given when client was started.
     *
     * @return the serverName.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Return a string representation of this client, including
     * the current messageToSend and messageReceived.
     *
     * @return a string representation of this client.
     */
    public String toString() {
        return "{class=Client|"
                + "|username=" + this.username
                + "|serverName=" + this.serverName
                + "|serverPort=" + this.serverPort
                + "|messageToSend={" + this.messageToSend.toString() + "}"
                + "|messageReceived={" + this.messageReceived.toString() + "}"
                + "}";
    }
}
