
import java.math.BigInteger;


/**
 * Class ReporterModel is responsible for receiving ciphertexts from the Leaker and prints the decrypted contents to
 * stdout
 *
 * @author Aidan Rubenstein
 * @version 01-May-2018
 */
public class ReporterModel implements LeakerListener {
    RSA rsa;

    /**
     * Constructor for a ReporterModel object
     *
     * @param rsa an instance of RSA that will decrypt the ciphertext
     */
    public ReporterModel(RSA rsa) {
        this.rsa = rsa;
    }

    /**
     * Attempts to convert the ciphertext to plaintext and prints the result. If the ciphertext is bad, an error is
     * printed to stderr
     *
     * @param ciphertext the ciphertext to be decrypted
     */
    public void leak(BigInteger ciphertext) {
        if(ciphertext == null) {
            System.err.print("ERROR\n");
        }

        try {
            System.out.println(rsa.decrypt(ciphertext));
        } catch(Exception e) {
            System.err.print("ERROR\n");
        }
    }
}