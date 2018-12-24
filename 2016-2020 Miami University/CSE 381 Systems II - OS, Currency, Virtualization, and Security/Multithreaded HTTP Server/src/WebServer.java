import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Usage: java WebServer PORT");
            return;
        }
        int port = Integer.valueOf(args[0]);

        ServerSocket welcomingSocket;
        try {
            welcomingSocket = new ServerSocket(port);
            System.out.printf("Listening on port %d\n", port);
        } catch (IOException e) {
            System.err.printf("Unable to listen for new connections on port %d\n", port);
            return;
        }

        while (true) {
            try {
                Socket connectionSocket = welcomingSocket.accept();
                System.out.printf("Got new connection from %s\n", connectionSocket.getInetAddress());

                HTTPRequest request = new HTTPRequest(connectionSocket);
                Thread handlingThread = new Thread(request);
                handlingThread.start();
            } catch (IOException e) {
                System.err.println("Unable to accept new connection");
            }
        }
    }
}
