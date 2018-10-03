import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


/**
 * Class LeakerProxy acts as a network proxy that accepts payloads to forward to the Reporter to display
 *
 * @author	Aidan Rubenstein
 * @version 01-May-2018
 */
public class LeakerProxy {
    byte[] payload;
    DatagramPacket packet;
    DatagramSocket mailbox;
    LeakerListener listener;

    /**
     * Constructor for a new LeakerProxy object
     *
     * @param mailbox mailbox from which the proxy receives data
     */
    public LeakerProxy(DatagramSocket mailbox) {
        this.mailbox = mailbox;
        this.payload = new byte[260];
    }

    /**
     * Sets the instance of LeakerListener to the current listener
     *
     * @param listener an instance of LeakerListener
     */
    public void setListener(LeakerListener listener) {
        this.listener = listener;
    }

    /**
     * Receives data packets from the mailbox, and then sends it to the listener to report
     */
    public void receive() {
        byte[] buf;
        int numRead;

        try {
            packet = new DatagramPacket(payload, payload.length);
            mailbox.receive(packet);

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload));

            buf = new byte[packet.getLength()];
            numRead = in.read(buf, 0, buf.length);
        } catch(IOException e) {
            System.err.print("Error: Failed to receive leaked message\n");
            return;
        }

        if(numRead == 0)
            listener.leak(null);
        else
            listener.leak(new BigInteger(buf));
    }
}