import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * Class RSA is responsible for acquiring the exponents from the first lines of the public/private file, as well
 * as performing encryption/decryption depending on what class calls upon it.
 *
 * @author	Aidan Rubenstein
 * @version 01-May-2018
 */
public class RSA {

    BigInteger pd;
    BigInteger d;
    BigInteger n;


    /**
     * Reads the public file and extracts its exponent and modulus respectively
     *
     * @param pub the name of the public file
     *
     * @throws IOException
     */
    public void readPub(String pub) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(pub));

            d = new BigInteger(reader.readLine());
            n = new BigInteger(reader.readLine());

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.print("Error: Cannot open " + pub);
            System.exit(1);
        }

    }

    /**
     * Reads the private file and extracts its exponent and modulus respectively
     *
     * @param priv the name of the private file
     *
     * @throws IOException
     */
    public void readPriv(String priv) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(priv));

            pd = new BigInteger(reader.readLine());
            n = new BigInteger(reader.readLine());

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Error: Cannot open " + priv);
            System.exit(1);
        }

    }

    /**
     * Performs the RSA encryption algorithm and returns the data as a byte array
     *
     * @param message the message to be encrypted
     *
     * @return a byte array of the now-encrypted message
     */
    public byte[] encrypt(String message) {
        OAEP encoder = new OAEP();


        byte[] seed = new byte[32];
        new SecureRandom().nextBytes(seed);

        BigInteger i = encoder.encode(message, seed);
        BigInteger j = i.modPow(d, n);

        return j.toByteArray();
    }

    /**
     * Performs the RSA decryption algorithm and returns the decrypted message
     *
     * @param ciphertext the message to be encrypted
     *
     * @return the string message to be reported
     */
    public String decrypt(BigInteger ciphertext) {
        OAEP oaep = new OAEP();

        return oaep.decode(ciphertext.modPow(pd, n));
    }


}