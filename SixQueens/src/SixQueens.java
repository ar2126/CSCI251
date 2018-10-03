/**
 * Class SixQueens acts on the client side as the main network program. It is responsible for connecting to the
 * correct host & port number with the player's name
 *
 * @author Aidan Rubenstein
 * @version 02-Apr-2018
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SixQueens{
    public static void main(String[] args) {
        String host = "";
        int port = 0;
        String playername = "";

        // Parsing command line inputs
        if (args.length != 3) {
            System.err.print("Error: Invalid argument length");
            System.exit(0);
        } else {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                playername = args[2];
            } catch (NumberFormatException e) {
                System.err.println("Error: Unable to parse command line inputs");
                System.exit(0);
            }

            try {
                // Open socket connection to server.
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port));

                // Set up view and model proxy.
                SixQueensView view = SixQueensView.create(playername);
                ModelProxy proxy = new ModelProxy(socket);
                view.setListener(proxy);
                proxy.setListener(view);

                // Inform server that a player has joined.
                proxy.join(view, playername);
            } catch (IOException exc) {
                System.err.print("Error: Connection issue");
                System.exit(0);
            }
        }
    }
}

