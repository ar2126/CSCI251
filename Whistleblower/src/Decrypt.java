import java.math.BigInteger;


/**
 * Class Decrypt is responsible for decrypting the ciphertext to plaintext using RSA with the help of class OAEP's
 * decode method
 *
 * @author	Aidan Rubenstein
 * @version 24-Apr-2018
 */

public class Decrypt {

    BigInteger d, n;

    /**
     * Constructor for a new Decrypt object
     *
     * @param d the exponent in decimal of the private file
     * @param n the modulus in decimal of the private file
     */
    public Decrypt(BigInteger d, BigInteger n) {
        this.d = d;
        this.n = n;
    }

    /**
     * Encrypts a message and returns the ciphertext as a byte array
     *
     * @param ciphertext a byte array representation of the encoded ciphertext
     *
     * @return the decoded message as plaintext
     *
     */
    public String decrypt(byte[] ciphertext) {
        OAEP oaep = new OAEP();

        BigInteger i = new BigInteger(ciphertext);
        BigInteger j = i.modPow(d, n);

        return oaep.decode(j);
    }
}