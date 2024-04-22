package src;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private TextField IPText;

    @FXML
    private TextField PortText;

    @FXML
    private Button connectBtn;

    @FXML
    private Button disconnectBtn;

    @FXML
    private TextArea msgHistory;

    @FXML
    private TextField msgInput;

    @FXML
    private Button sendBtn;

    @FXML
    private TextField nameText;

    @FXML
    private Label statusLabel;

    
    @FXML
    private Button enterBtn;



    private Client client;

    String username;
    String serverIP;
    int port;

    // Method to set the Client instance
    public void setClient(Client client) {
        this.client = client;
        setupBindings(); // Set up bindings after the client is set
    }



    public void setupBindings() {
        // sendBtn.disableProperty().bind(client.readyProperty().not());
        connectBtn.setDisable(true);
        sendBtn.setDisable(true);
        msgHistory.setDisable(true);
        disconnectBtn.setDisable(true);
        IPText.setDisable(true);
        PortText.setDisable(true);
        msgInput.setDisable(true);
        
    }

    public void updateStatus(String message) {
        statusLabel.setText(message); // Assume there's a Label for status messages
    }

    
    @FXML
    void sendMsgAction(ActionEvent event) {
        // Save whatever was typed into the sendMsgField

        try {
            String message = msgInput.getText();
            // call the sendMessage method of the Client instance
            client.sendMessage(message);
        }
        catch (Exception e) {
            System.out.println("Issue getting msgInput!!");
        }

        // clear the input field after sending the message
        msgInput.clear();

    }


    @FXML
    void handleConnect(ActionEvent event) {
        serverIP = IPText.getText(); 
        try {
            port = Integer.parseInt(PortText.getText());
        } catch (NumberFormatException e) {
            updateStatus("Error: Port must be a number.");
            return;
        }

        client.connect();
        connectBtn.setDisable(true); // Disable during the connection attempt
    }

    // @FXML
    // void handleConnect(ActionEvent event) {
    //     //retrieve the server IP and port number from the GUI and
    //     //use these fields to connect to the server
    //     serverIP = IPText.getText(); 
    //     port = Integer.parseInt(PortText.getText());

    //     client.connect();

    //     // connectBtn.setDisable(true);
    //     disconnectBtn.setDisable(false);
    //     sendBtn.setDisable(false);
    //     msgInput.setDisable(false);

    // }

    public void enableChatControls(boolean enable) {
        sendBtn.setDisable(!enable);
        disconnectBtn.setDisable(!enable);
        msgInput.setDisable(!enable);
    }

    public void enableConnectButton(boolean enable) {
        connectBtn.setDisable(!enable);
    }

    String getIPText() {
        return IPText.getText();
    }
    Integer getPort() {
        return Integer.parseInt(PortText.getText());
    }

    @FXML
    void acceptUsername(ActionEvent event) {
        connectBtn.setDisable(true);
        username = nameText.getText();
        client.setUsername(username);

        enterBtn.setDisable(true);
        connectBtn.setDisable(false);
        IPText.setDisable(false);
        PortText.setDisable(false);

    }

    String getUsername () {
        return nameText.getText();
    }


    @FXML
    void handleDisconnect(ActionEvent event) {
        //initiate disconnecting from the server
        client.disconnect();

    }

    public void updateChatHistory(String message) {
        Platform.runLater(() -> {
            msgHistory.appendText(message + "\n");
        });
    }


}