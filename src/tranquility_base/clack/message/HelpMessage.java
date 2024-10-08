package tranquility_base.clack.message;

import java.util.Objects;

/**
 * This class represents the user requesting help. The client
 * should respond by printing a local help message; there is
 * no need to send the message to the server.
 */
public class HelpMessage extends Message {

    public static final String HELP = "Commands: \n"
//            + "    ENCRYPTION KEY key\n"
//            + "    ENCRYPTION ON|OFF\n"
            + "    HELP\n"
            + "    LIST USERS\n"
            + "    LOGOUT\n"
            + "    SEND FILE filepath {AS filename}\n"
            + "  Anything else is a text message.";

    // Situation-specific help or error text
    private final String extraHelp;

    public HelpMessage(String username, String extraHelp) {
        super(username, MSGTYPE_HELP);
        this.extraHelp = extraHelp;
    }

    public HelpMessage(String username) {
        this(username, "");
    }

    @Override
    public String[] getData() {
        if (extraHelp.isEmpty()) {
            return new String[] {HELP};
        } else {
            return new String[] {extraHelp + "\n" + HELP};
        }
    }

    /**
     * Equality comparison. Returns true iff the other object is of
     * the same class and all fields (including those inherited from
     * superclasses) are equal.
     *
     * @param o the object to test for equality.
     * @return whether o is of the same class as this, and all fields
     * are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        HelpMessage that = (HelpMessage) o;
        return Objects.equals(this.getTimestamp(), that.getTimestamp())
                && Objects.equals(this.getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return "{class=HelpMessage|"
                + super.toString()
                + "|help=" + getData()[0]
                + "}";
    }
}
