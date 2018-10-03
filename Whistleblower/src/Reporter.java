import java.io.*;
import java.lang.NumberFormatException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/**
 * Class Reporter contains the main method for the 'Reporter' portion of the Whilstleblower project. It is used for
 * converting the datagram's payload to the ciphertext, decrypting the ciphertext using RSA, yielding the plaintext,
 * and decoding the plaintext, yielding the message string.
 *
 * @author	Aidan Rubenstein
 * @version 24-Apr-2018
 */
public class Reporter {
    public static String rhost;
    public static int rport = 0;
    public static String privatefile;
    public static BigInteger pExp;
    public static BigInteger modulus;
    public static Decrypt d;

    /**
     * Main method for Reporter.It converts the datagram's payload to the ciphertext, decrypts the ciphertext using RSA,
     * yielding the plaintext, and decodes the plaintext, yielding the message string.
     *
     * @param args The specified program arguments
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //Checks argument lengths and parses all arguments as intended
        if (args.length != 3) {
            System.err.print("ERROR: Insufficient number of arguments");
            System.exit(1);
        }
        try {
            rhost = args[0];

            rport = Integer.parseInt(args[1]);

            privatefile = args[2];
        }catch(NumberFormatException e){
            System.err.print("ERROR: one or more arguments cannot be parsed");
            System.exit(1);
        }

        // Extracts the exponent and modulus from the 2 lines in the private key file as 2048-bit integers
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(privatefile));

            pExp = new BigInteger(reader.readLine());
            modulus = new BigInteger(reader.readLine());

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.print("ERROR: Private key file " + privatefile + " does not exist\n");
            System.exit(1);
        }

        //Converts the ciphertext to plaintext using Decrypt
        d = new Decrypt(pExp, modulus);

        byte[] payload = new byte[260];
        DatagramPacket pay;
        DatagramSocket mailbox = new DatagramSocket(
                new InetSocketAddress(rhost, rport));

        //Opens the Reporter's mailbox to receive any messages from the Leaker to report
        while(true) {
            pay = new DatagramPacket(payload, payload.length);

            try {
                mailbox.receive(pay);
            } catch(Exception e) {

            }

            try {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload));
                short size = in.readShort();

                if(size < 1 || size > 258)
                    throw new Exception();

                byte[] buf = new byte[size];

                if(size != in.read(buf, 0, size))
                    throw new Exception();
                //Prints the final decrypted message to the console
                System.out.print(d.decrypt(new BigInteger(buf).toByteArray()) + "\n");
            }
            //If any errors occurs during the transaction of receiving messages from Leaker, an error message is printed
            //to standard out
            catch(Exception e) {
                System.err.print("ERROR\n");
            }
        }
    }
}