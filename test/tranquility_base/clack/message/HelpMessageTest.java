package tranquility_base.clack.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.*;
import static org.junit.jupiter.api.Assertions.*;

class HelpMessageTest {

    HelpMessage msg1;       // from 1-arg constructor
    HelpMessage msg2;       // from 2-arg constructor
    LocalDateTime now;

    final String USERNAME = "the user";
    final String EXTRA_HELP = "some extra help";

    @BeforeEach
    void setUp() {
        msg1 = new HelpMessage(USERNAME);
        msg2 = new HelpMessage(USERNAME, EXTRA_HELP);
    }

    @Test
    void getTimestamp() {
        now = LocalDateTime.now();
        Duration duration = between(msg1.getTimestamp(), now);
        long timeDiff = Math.abs(duration.toSeconds());
        assertTrue(timeDiff <= 1);

        now = LocalDateTime.now();
        duration = between(msg2.getTimestamp(), now);
        timeDiff = Math.abs(duration.toSeconds());
        assertTrue(timeDiff <= 1);
    }

    @Test
    void getMsgType() {
        assertEquals(Message.MSGTYPE_HELP, msg1.getMsgType());
        assertEquals(Message.MSGTYPE_HELP, msg2.getMsgType());
    }

    @Test
    void getUsername() {
        assertEquals(USERNAME, msg1.getUsername());
        assertEquals(USERNAME, msg2.getUsername());
    }

    @Test
    void testHashCode() {
        assertNotEquals(msg1.hashCode(), msg2.hashCode());
    }

    @Test
    void testToString() {
        String msg1str = "{class=HelpMessage"
                + "|timestamp=" + msg1.getTimestamp()    // cheating, but only way to get it right
                + "|username=" + USERNAME
                + "|help=" + HelpMessage.HELP
                + "}";
        assertEquals(msg1str, msg1.toString());

        String msg2str = "{class=HelpMessage"
                + "|timestamp=" + msg2.getTimestamp()    // cheating, but only way to get it right
                + "|username=" + USERNAME
                + "|help=" + EXTRA_HELP + "\n" + HelpMessage.HELP
                + "}";
        assertEquals(msg2str, msg2.toString());
    }

    @Test
    void getData() {
        assertEquals(1, msg1.getData().length);
        assertEquals(HelpMessage.HELP, msg1.getData()[0]);

        assertEquals(1, msg2.getData().length);
        assertEquals(EXTRA_HELP + "\n" + HelpMessage.HELP,
                msg2.getData()[0]);
    }

    @Test
    void testEquals() {
        assertTrue(msg1.equals(msg1));
        assertFalse(msg1.equals(null));
        assertFalse(msg1.equals(new HelpMessage("some other user")));
        assertFalse(msg1.equals(msg2));

        assertTrue(msg2.equals(msg2));
        assertFalse(msg2.equals(null));
        assertFalse(msg2.equals(new HelpMessage("some other user")));
        assertFalse(msg2.equals(new HelpMessage(USERNAME, "some other extra")));
        assertFalse(msg2.equals(msg1));
    }

}