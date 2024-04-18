package src;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ServerController {

    @FXML
    private TextArea msgHistory;

    @FXML 
    private TextField PortText;

    @FXML 
    private Button startServerBtn;

    @FXML
    private Button stopServerBtn;

    private Server server;
    int port;

    public void updateChatHistory(String message) {
        Platform.runLater(() -> {
            msgHistory.appendText(message + "\n");
        });
    }

    @FXML
    public void startServer(ActionEvent event)
    {
        //use the PortText variable to get the port and use it
        //to start running the server w that port
        port = Integer.parseInt(PortText.getText());
        server.startServer();

        // prevent the user from clicking start more than once
        startServerBtn.setDisable(true);
    }

    @FXML 
    public void stopServer(ActionEvent event)
    {
        
        //stop the server from using that port and "running"
        server.closeServerSocket();
        startServerBtn.setDisable(false);

        
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Integer getPort() {
        return port;
    }
}