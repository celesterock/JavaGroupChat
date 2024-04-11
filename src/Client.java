package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

// "extends Application" indicates that Client is a javaFX application
public class Client extends Application {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private ClientController controller;

    public Client() {
        // Initialize default values for the fields if necessary
        this.socket = null;
        this.bufferedReader = null;
        this.bufferedWriter = null;
        this.username = "DefaultUsername";
    }
    
    private BooleanProperty readyProperty = new SimpleBooleanProperty(false);
    
    // Getter for the property
    public BooleanProperty readyProperty() {
        return readyProperty;
    }

    // Method to update the property's value
    public void setReady(boolean ready) {
        this.readyProperty.set(ready);
    }

    // // Update your constructor or initialization method to set this property to true once everything is set up
    // public Client(Socket socket, String username) {
    //     try {
    //         this.socket = socket;
    //         this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    //         this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //         this.username = username;
    //         // Assuming everything is initialized correctly
    //         setReady(true);
    //     } catch (IOException e) {
    //         closeEverything(socket, bufferedReader, bufferedWriter);
    //     }
    // }

    public void sendMessage(String messageToSend) {
        try {
        // Check if bufferedWriter is null or socket is not connected
        if (bufferedWriter == null || socket == null || !socket.isConnected() || socket.isClosed()) {
            System.err.println("Error: Unable to send message. Connection is not established.");
            return;
        }

            // sends the message to the server (server will then send to other clients)
            bufferedWriter.write(username + ": " + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void listenForMessages() {
        // creating a new thread object and calling start on it
        new Thread (new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                    // Update chat history using the controller
                    if (msgFromGroupChat != null) {
                        controller.updateChatHistory(msgFromGroupChat);
                    }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ask for the username here, since we're in the JavaFX thread and can show dialogs/popups if needed
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
    
        // Now initialize the socket here, so the 'Client' instance created by JavaFX is the one that's fully initialized
        try {
            Socket socket = new Socket("localhost", 1234);
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            setReady(true); // This Client instance is now ready
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatClient.fxml"));
        Parent root = loader.load();
        this.controller = loader.getController();
        controller.setClient(this); // Pass this fully initialized Client instance to the controller
    
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Group Chat");
        primaryStage.show();
    
        // Start listening for messages
        listenForMessages();
    }
    



    // must define the start() method for javaFX applications: initializes and displayes the GUI
    // @Override
    // public void start(Stage primaryStage) throws Exception {
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatClient.fxml"));
    //     Parent root = loader.load();
    //     ClientController controller = loader.getController();
    //     controller.setClient(this); // Pass the Client instance to the controller
    //     controller.setupBindings(); // Setup bindings after the client is set


    //     primaryStage.setScene(new Scene(root));
    //     primaryStage.setTitle("Group Chat");
    //     primaryStage.show();
    // }

    public static void main(String[] args) throws IOException {
        // // Get username input from the user
        // Scanner scanner = new Scanner(System.in);
        // System.out.println("Enter your username for the group chat: ");
        // String username = scanner.nextLine();
    
        // // Create a socket and initialize the client object
        // Socket socket = new Socket("localhost", 1234);
        // Client client = new Client(socket, username);
    
        // // Start listening for messages from the server in a separate thread
        // client.listenForMessages();
    
        // Launch the JavaFX application
        launch(args);
    }
    
}