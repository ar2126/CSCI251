
import java.io.IOException;
import java.lang.NumberFormatException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/**
 * Class Leaker contains the main method for the 'Leaker' portion of the Whilstleblower project. It parses the input
 * from the command line, encodes the message to plaintext, calls RSA to read files &
 * convert the plaintext to ciphertext, and calls upon the ReporterProxy to create & send a payload
 *
 * @author	Aidan Rubenstein
 * @version 01-May-2018
 */

public class Leaker {

    private static String rhost;
    private static int rport = 0;
    private static String lhost;
    private static int lport = 0;
    private static String file;
    private static String message;

    /**
     * Main method for Leaker.It parses the input from the command line, encodes the message to plaintext, calls
     * RSA to convert the plaintext to ciphertext, and calls upon the ReporterProxy to create & send a payload
     *
     * @param args The specified program arguments
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 6) {
            System.err.println("Usage: java Leaker <rhost> <rport> <lhost> <lport> <publickeyfile> <message>");
            System.exit(1);
        }

        rhost = args[0];

        try {
            rport = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.err.print("Error: Cannot parse " + args[1]);
            System.exit(1);
        }

        lhost = args[2];

        try {
            lport = Integer.parseInt(args[3]);
        } catch(NumberFormatException e) {
            System.err.print("Error: Cannot parse " + args[3]);
            System.exit(1);
        }

        file = args[4];


        message = args[5];

        RSA rsa = new RSA();
        rsa.readPub(file);
        byte[] ciphertext = rsa.encrypt(message);

        ReporterProxy proxy = new ReporterProxy(
                new DatagramSocket(new InetSocketAddress(lhost, lport)),
                new InetSocketAddress(rhost, rport));

        proxy.leak(new BigInteger(ciphertext));
    }
}