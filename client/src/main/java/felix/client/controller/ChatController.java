package felix.client.controller;

import felix.client.models.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController extends MainController
{
    @FXML private Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.setStage(this.button));
    }

    public void click()
    {
        super.openNewView(View.HOME);
    }
}