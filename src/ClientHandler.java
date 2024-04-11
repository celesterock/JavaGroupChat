package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    // static array list of every client that has been instantiated
    // keeps track of every client so that when one client sends a message, it can be sent to all others by looping through this list
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    // socket passed from the server class
    private Socket socket;

    // used to read messages that have been sent from the client
    private BufferedReader bufferedReader;

    // used to send messages to the client
    private BufferedWriter bufferedWriter;

    private String clientUsername;
    
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;

            // OutputStreamWriter lets us send character streams (as opposed to bytes)
            // Buffer makes communication more efficient?
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // client enters name and presses enter
            this.clientUsername = bufferedReader.readLine();

            // add the client to the array list
            clientHandlers.add(this);

            // let the other connect clients know that someone has joinged
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        // listens for messages from client while there is a connection
        while(socket.isConnected()) {
            try {
                // program will hold here until we receive a message from the client
                messageFromClient = bufferedReader.readLine(); 
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                
                // break out of while loop when client disconnects
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        // for each clientHandler in our array list....
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // send message to all other clients (except the one that sent the message)
                if(!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    // sends a newline (acts like pressing the <enter> key)
                    clientHandler.bufferedWriter.newLine();

                    // fills the entire buffer so that message is sent
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }

        }
    }

    public void removeClientHandler() {
        // remove the current clientHandler (this) from the list
        clientHandlers.remove(this);

        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}