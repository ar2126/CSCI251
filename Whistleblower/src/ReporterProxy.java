import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;


/**
 * Class ReporterProxy acts as a network proxy for communicating between the Leaker & sends payloads to the Reporter's
 * mailbox
 *
 * @author	Aidan Rubenstein
 * @version 01-May-2018
 */
public class ReporterProxy implements LeakerListener {
    DatagramSocket mailbox;
    SocketAddress rMailbox;

    /**
     * Constructor for a new ReporterProxy object
     *
     * @param mailbox the target mailbox(Reporter)
     * @param rMailbox the SocketAddress for the Reporter Mailbox
     */
    public ReporterProxy(DatagramSocket mailbox, SocketAddress rMailbox) {
        this.mailbox = mailbox;
        this.rMailbox = rMailbox;
    }

    /**
     * Sends the encrypted message to the Reporter's mailbox. Prints an error to stderr if and exceptions are found
     *
     * @param ciphertext the ciphertext being sent
     */
    public void leak(BigInteger ciphertext) {
        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outByte);

        try {
            byte[] buf = ciphertext.toByteArray();

            out.write(buf, 0, buf.length);
            out.close();

            byte[] payload = outByte.toByteArray();
            mailbox.send(new DatagramPacket(payload, payload.length, rMailbox));
        } catch(IOException e) {
            System.err.print("Error: Failed to leak message\n");
        }
    }
}