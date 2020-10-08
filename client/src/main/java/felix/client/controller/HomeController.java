package felix.client.controller;

import felix.client.main.FelixSession;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController extends MainController
{
    @FXML private Pane paneLogout;
    @FXML private Button restButton;
    @FXML private Button button;
    private IUserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.setStage(this.button));
        this.listenPrivateChat();
    }

    private void initializeEvents()
    {
        paneLogout.addEventHandler(MouseEvent.MOUSE_CLICKED, key ->
        {
//            this.logout();
        });
    }

    private void listenPrivateChat()
    {
        restButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            userService.rest();
        });
    }

    public void click()
    {
        FelixSession.getInstance().sendMessage("Hey!");
        //super.openNewView(View.CHAT);
    }
}