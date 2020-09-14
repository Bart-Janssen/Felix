package felix.client.controller;

import felix.client.models.View;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends MainController
{
    @FXML private Button button;
    private IUserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.setStage(this.button));
        this.listenPrivateChat();
    }

    private void listenPrivateChat()
    {
        connectToServer("ws://localhost:6666/server/");
    }

    public void click()
    {
        session.getAsyncRemote().sendText("Hey");
        //super.openNewView(View.CHAT);
    }
}