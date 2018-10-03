import java.math.BigInteger;

/**
 * Interface LeakerListener is responsible for handling sent messages to the Reporter to be decrypted/shared
 *
 * @author Aidan Rubenstein
 * @version 01-May-2018
 */
public interface LeakerListener {
   void leak(BigInteger ciphertext);
}