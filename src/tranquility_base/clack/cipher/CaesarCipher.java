package tranquility_base.clack.cipher;

import java.util.Arrays;
import java.util.Objects;

public class CaesarCipher {

    public static final String DEFAULT_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final int key;
    private final String alphabet;

    /**
     * Constructs a CaesarCipher object with the given key
     * and alphabet.
     * @param key the key for the cipher
     * @param alphabet the characters that the cipher will encrypt/decrypt.
     * @throws IllegalArgumentException if key == 0, or alphabet is null or empty.
     */
    public CaesarCipher(int key, String alphabet) {
        if (key == 0) {
            throw new IllegalArgumentException("key of zero not allowed");
        }
        if (alphabet == null || alphabet.isEmpty()) {
            throw new IllegalArgumentException("empty alphabet not allowed");
        }

        // Check for duplicate chars in alphabet
        char[] chars = alphabet.toCharArray();
        Arrays.sort(chars);
        for (int i = 0; i < chars.length - 1; ++i) {
            if (chars[i] == chars[i + 1]) {
                throw new IllegalArgumentException("duplicate chars in alphabet");
            }
        }

        // Ensure key is in range 0 .. alphabet.length() - 1.
        this.key = ((key % alphabet.length()) + alphabet.length()) % alphabet.length();
        this.alphabet = alphabet;
    }

    /**
     * Constructs a CaesarCipher object with the given key
     * and using the default alphabet.
     * @param key the key for the cipher
     */
    public CaesarCipher(int key) {
        this(key, DEFAULT_ALPHABET);
    }

    /**
     * Returns this cipher's alphabet.
     * @return the alphabet.
     */
    public String getAlphabet() {
        return this.alphabet;
    }

    /**
     * Encrypts a string, using the cipher's key and alphabet.
     * Characters not in the alphabet are preserved.
     * @param clearText the string to encrypt.
     * @return the encryption of the cleartext.
     */
    public String encrypt(String clearText) {
        return shiftChars(clearText, key);
    }

    /**
     * Decrypts a string, using the cipher's key and alphabet.
     * Characters not in the alphabet are preserved.
     * @param cipherText the string to decrypt.
     * @return the decryption of the ciphertext.
     */
    public String decrypt(String cipherText) {
        return shiftChars(cipherText, -key);
    }

    /**
     * Returns the string str, but with each character
     * shifted along the alphabet by the given amount.
     * Characters in the string that are not in the alphabet
     * are left as-is. If str is null, the null string is returned.
     * @param str the string to shift.
     * @param shift the amount of the shift.
     * @return the string str, but with all alphabet
     * characters shifted.
     */
    private String shiftChars(String str, int shift) {
        if (str == null) {
            return null;
        }

        char[] shiftedChars = new char[str.length()];

        // Ensure shift is in range 0 .. alphabet.length() - 1.
        shift = ((shift % alphabet.length()) + alphabet.length()) % alphabet.length();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            int loc = alphabet.indexOf(ch);
            if (loc == -1) {
                shiftedChars[i] = ch;
            } else {
                int shiftedLoc = (loc + shift) % alphabet.length();
                shiftedChars[i] = alphabet.charAt(shiftedLoc);
            }
        }
        return new String(shiftedChars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaesarCipher that = (CaesarCipher) o;
        return key == that.key && Objects.equals(alphabet, that.alphabet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, alphabet);
    }
}
