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
        try
        {
            super.connectToServer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            super.setJwtToken(null);
            super.openNewView(View.LOGIN);
        }
    }

    public void click()
    {
        super.session.getAsyncRemote().sendText("Hey");
        //super.openNewView(View.CHAT);
    }
}