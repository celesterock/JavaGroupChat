package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application {
    private ServerSocket serverSocket;
    private ServerController serverController;
    Stage stage;

    public Server() {}

    // This constructor might not be necessary unless you plan to create instances manually with specific server sockets
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            // Start the server in a new thread to avoid blocking JavaFX thread
            new Thread(() -> {
                try {
                    // random default port
                    int port = 0;

                    port = serverController.getPort();
                    // Initialize server socket
                    serverSocket = new ServerSocket(port); 
                    System.out.println("Successfully started new server at port " + port);
                    while (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        System.out.println("A new client has connected.");
                        
                        
                        // creates a new thread for each client that connects to the server
                        // Note: there is a ClientHandler for each client, but also a static array list of Client 
                        //       handlers in the ClientHandler class so that they can communicate with each other
                        ClientHandler clientHandler = new ClientHandler(socket, serverController);
                        Thread thread = new Thread(clientHandler);

                        // begin the execution of the thread
                        thread.start();
                    }

                } catch (IOException e) {
                    closeServerSocket();
                }
            }).start();
        } catch (Exception e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerClient.fxml"));
        Parent root = loader.load();

        // each client handler needs a ServerController so that it can pass the message to the ServerController class to update the server GUI
        this.serverController = loader.getController();
        serverController.setServer(this);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
