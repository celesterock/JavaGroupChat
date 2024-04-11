package src;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // listens for incoming connections from clients and creates a socket object to communicate with them
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            // continually run server
            while (!serverSocket.isClosed()) {
                // wait for a client to connect (program is halted until client connects)
                // create a new socket object for client when they connect
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");

                // spawns a new thread for each client
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);

                // begin the execution of the thread
                thread.start();

            }

        }catch(IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            // prevents nullptr exception
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        Server server = new Server(serverSocket);
        server.startServer();
    }

}