package tranquility_base.clack.cipher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CaesarCipherTest {

    @Test
    void badConstructorArgs() {
        Exception e = null;

        e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CaesarCipher(0));
        Assertions.assertEquals("key of zero not allowed", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CaesarCipher(0, "ABCD"));
        Assertions.assertEquals("key of zero not allowed", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CaesarCipher(1, null));
        Assertions.assertEquals("empty alphabet not allowed", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CaesarCipher(1, ""));
        Assertions.assertEquals("empty alphabet not allowed", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CaesarCipher(1, "ABCAD"));
        Assertions.assertEquals("duplicate chars in alphabet", e.getMessage());
    }

    @Test
    void getAlphabet() {
        CaesarCipher cc = new CaesarCipher(5);
        Assertions.assertEquals(CaesarCipher.DEFAULT_ALPHABET, cc.getAlphabet());

        cc = new CaesarCipher(-442, "xyz ,.");
        Assertions.assertEquals("xyz ,.", cc.getAlphabet());
    }

    @Test
    void encrypt() {
        String msg = "The quick, brown fox jumped. Over the lazy poodle!";

        CaesarCipher cc = new CaesarCipher(1);
        // Ignore all but CAPITAL LETTERS.
        Assertions.assertEquals("Uhe quick, brown fox jumped. Pver the lazy poodle!",
                cc.encrypt(msg));
        // Message in all caps.
        String msgINCAPS = msg.toUpperCase();
        Assertions.assertEquals("UIF RVJDL, CSPXO GPY KVNQFE. PWFS UIF MBAZ QPPEMF!",
                cc.encrypt(msgINCAPS));
        // Expanded alphabet.
        cc = new CaesarCipher(1, CaesarCipher.DEFAULT_ALPHABET
                + CaesarCipher.DEFAULT_ALPHABET.toLowerCase()
                + " .,");
        Assertions.assertEquals("Uif.rvjdlA.cspxo.gpy.kvnqfe,.Pwfs.uif.mb z.qppemf!",
                cc.encrypt(msg));
    }

    @Test
    void decrypt() {
        String msg = "The quick, brown fox jumped. Over the lazy poodle!";

        CaesarCipher cc = new CaesarCipher(1);
        // Ignore all but CAPITAL LETTERS.
        Assertions.assertEquals(msg,
                cc.decrypt("Uhe quick, brown fox jumped. Pver the lazy poodle!"));
        // Message in all caps.
        String msgINCAPS = msg.toUpperCase();
        Assertions.assertEquals(msgINCAPS,
                cc.decrypt("UIF RVJDL, CSPXO GPY KVNQFE. PWFS UIF MBAZ QPPEMF!"));
        // Expanded alphabet.
        cc = new CaesarCipher(1, CaesarCipher.DEFAULT_ALPHABET
                + CaesarCipher.DEFAULT_ALPHABET.toLowerCase()
                + " .,");
        Assertions.assertEquals(msg,
                cc.decrypt("Uif.rvjdlA.cspxo.gpy.kvnqfe,.Pwfs.uif.mb z.qppemf!"));

    }
}