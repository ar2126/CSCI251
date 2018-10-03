
import java.io.IOException;
import java.lang.NumberFormatException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


/**
 * Class Reporter contains the main method for the 'Reporter' portion of the Whilstleblower project. It is used for
 * converting the datagram's payload to the ciphertext, reading the private file & decrypting the ciphertext using RSA,
 * yielding the plaintext, and calling on the ListenerProxy to receive messages and report them
 *
 * @author	Aidan Rubenstein
 * @version 01-May-2018
 */

public class Reporter {
    private static String rhost;
    private static int rport = 0;
    private static String file = "";

    /**
     * Main method for Reporter. It converts the datagram's payload to the ciphertext, reads the private file &
     * decrypts the ciphertext using RSA, yielding the plaintext, and calls on the ListenerProxy to
     * receive messages and report them
     *
     * @param args The specified program arguments
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            System.err.print("Usage: java Reporter <rhost> <rport> <privatefile>");
            System.exit(1);
        }

        rhost = args[0];

        try {
            rport = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.err.print("Error: Cannot parse " + args[1]);
            System.exit(1);
        }
        file = args[2];

        RSA rsa = new RSA();
        rsa.readPriv(file);

        ReporterModel model = new ReporterModel(rsa);
        LeakerProxy proxy = new LeakerProxy(
                new DatagramSocket(new InetSocketAddress(rhost, rport)));

        proxy.setListener(model);

        while(true)
            proxy.receive();
    }
}