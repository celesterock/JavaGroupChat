package src;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private TextArea msgHistory;

    @FXML
    private TextField msgInput;

    @FXML
    private Button sendBtn;

    @FXML
    private Button connectBtn;

    @FXML
    private Button disconnectBtn;

    private Client client;

    // Method to set the Client instance
    public void setClient(Client client) {
        this.client = client;
        setupBindings(); // Set up bindings after the client is set
    }



    public void setupBindings() {
        sendBtn.disableProperty().bind(client.readyProperty().not());
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
        //retrieve the server IP and port number from the GUI and
        //use these fields to connect to the server

    }

    @FXML
    void handleDisconnect(ActionEvent event) {
        //initiate disconnecting from the server

    }

    public void updateChatHistory(String message) {
        Platform.runLater(() -> {
            msgHistory.appendText(message + "\n");
        });
    }


}