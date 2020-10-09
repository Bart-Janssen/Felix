package felix.client.controller;

import felix.client.fxml.Chat;
import felix.client.fxml.Friend;
import felix.client.main.FelixSession;
import felix.client.models.User;
import felix.client.service.friend.FriendService;
import felix.client.service.friend.IFriendService;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class HomeController extends MainController
{
    @FXML private VBox vBoxChats;
    @FXML private VBox friends;
    @FXML private Button restButton;
    @FXML private Button button;
    private IUserService userService = new UserService();
    private IFriendService friendService = new FriendService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.setStage(this.button));
        this.listenPrivateChat();
        Platform.runLater(() -> friends.getChildren().addAll(this.setFriendsToFriendPanel()));
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friendDisplayName -> friends.add(new Friend(friendDisplayName, false,(event -> this.openChat(friendDisplayName))))); //todo event handler lol
        return friends;
    }

    private void openChat(String friendDisplayName)
    {
        vBoxChats.getChildren().clear();
        vBoxChats.getChildren().add(new Chat(friendDisplayName));
    }

    private List<String> getFriends()
    {
        try
        {
            return this.friendService.getFriends();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void listenPrivateChat()
    {
        restButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            userService.rest();
        });
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            FelixSession.getInstance().sendMessage("Hey!");
        });
    }
}