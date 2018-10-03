import java.io.*;
import java.lang.NumberFormatException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * Class Leaker contains the main method for the 'Leaker' portion of the Whilstleblower project. It is used for
 * parsing the input from the command line, encoding the message to plaintext, calling Encrypt to convert the plaintext
 * to ciphertext, and converting the ciphertext into the payload for the datagram
 *
 * @author	Aidan Rubenstein
 * @version 24-Apr-2018
 */
public class Leaker {

    public static String rhost;
    public static String lhost;
    public static int rport = 0;
    public static int lport = 0;
    public static String publicfile;
    public static String message;
    public static BigInteger exponent;
    public static BigInteger modulus;

    /**
     * Main method for Leaker.It parses the input from the command line, encodes the message to plaintext, calls
     * Encrypt to convert the plaintext to ciphertext, and converts the ciphertext into the payload for the datagram
     *
     * @param args The specified program arguments
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //Checks argument lengths and parses all arguments as intended
        if (args.length != 6) {
            System.err.print("ERROR: Insufficient number of arguments");
            System.exit(1);
        }
        try {

            rhost = args[0];

            rport = Integer.parseInt(args[1]);

            lhost = args[2];

            lport = Integer.parseInt(args[3]);

            publicfile = args[4];

        } catch (NumberFormatException e){
            System.err.print("ERROR: one or more arguments cannot be parsed");
            System.exit(1);
        }

        // Extracts the exponent and modulus from the 2 lines in the public key file as 2048-bit integers
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(publicfile));

            exponent = new BigInteger(reader.readLine());
            modulus = new BigInteger(reader.readLine());

            reader.close();
        } catch (FileNotFoundException e) {
            System.err.print("ERROR: Public key file " + publicfile + " does not exist\n");
            System.exit(1);
        }
        message = args[5];

        //Converts the plaintext to ciphertext using Encrypt
        Encrypt e = new Encrypt(exponent, modulus);
        byte[] ciphertext = e.encrypt(message);

        DatagramSocket mailbox = new DatagramSocket(
                new InetSocketAddress(lhost, lport));

        SocketAddress rMailbox = new InetSocketAddress(rhost, rport);

        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outByte);

        //Writes the ciphertext containing the length of the ciphertext and the ciphertext itself and sends it to
        //the payload as a byte array
        out.writeShort(ciphertext.length);
        out.write(ciphertext, 0, ciphertext.length);
        out.close();

        //Creates a payload as a byte array and sends the payload to the Reporter
        byte[] payload = outByte.toByteArray();
        mailbox.send(new DatagramPacket(payload, payload.length, rMailbox));
    }
}