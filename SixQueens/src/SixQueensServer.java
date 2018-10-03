/**
 * Class SixQueensServer establishes a connection at a given host and port number and listens for connections.
 * This is the main server program.
 *
 * @author Aidan Rubenstein
 * @version 02-Apr-2018
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SixQueensServer {
    public static void main(String[] args){
        String host = "";
        int port = 0;
        //Parsing command line inputs
        if(args.length != 2){
            System.err.print("Error: Invalid argument length");
            System.exit(0);
        }
        else{
            try{
                host = args[0];
                port = Integer.parseInt(args[1]);

            }
            catch(NumberFormatException e){
                System.err.println("Error: Unable to parse command line inputs");
                System.exit(0);
            }

            try{
                ServerSocket serversocket = new ServerSocket();
                serversocket.bind (new InetSocketAddress (host, port));

                // Session management logic.
                SixQueensModel model = null;
                for (;;)
                {
                    Socket socket = serversocket.accept();
                    ViewProxy proxy = new ViewProxy (socket);
                    if (model == null || model.isFinished())
                    {
                        model = new SixQueensModel();
                        proxy.setListener (model);
                    }
                    else
                    {
                        proxy.setListener (model);
                        model = null;
                    }
                }
            }
            catch(IOException exec){
                System.err.print("Error: Connection Issue");
                System.exit(0);
            }
        }
    }
}
