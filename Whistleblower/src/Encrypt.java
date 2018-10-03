import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * Class Encrypt is responsible for encrypting the plaintext using RSA with the help of the OAEP class' encode method
 *
 * @author	Aidan Rubenstein
 * @version 24-Apr-2018
 */
public class Encrypt {

    BigInteger d, n;

    /**
     * Constructor for a new Encrypt object
     *
     * @param d the exponent in decimal of the public file
     * @param n the modulus in decimal of the public file
     */
    public Encrypt(BigInteger d, BigInteger n) {
        this.d = d;
        this.n = n;
    }

    /**
     * Encrypts a message and returns the ciphertext as a byte array
     *
     * @param message the message to be encoded
     *
     * @return byte[] a byte array representation of the encoded ciphertext
     */
    public byte[] encrypt(String message) {
        OAEP oaep = new OAEP();

        byte[] seed = new byte[32];
        new SecureRandom().nextBytes(seed);

        BigInteger i = oaep.encode(message, seed);
        BigInteger j = i.modPow(d, n);

        return j.toByteArray();
    }
}