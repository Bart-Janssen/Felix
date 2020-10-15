package felix.controllers;

import felix.fxml.Chat;
import felix.fxml.Friend;
import felix.main.Felix;
import felix.main.FelixSession;
import felix.models.WebSocketMessage;
import felix.service.friend.FriendService;
import felix.service.friend.IFriendService;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends MainController implements IController
{
    @FXML private ScrollPane scrollPaneChats;
    @FXML private TextField textFieldMessage;
    @FXML private Button buttonSend;
    @FXML private VBox friends;
    @FXML private Button restButton;
    @FXML private Button button;
    private VBox vBoxChats = new VBox(5);
    private IUserService userService = new UserService();
    private IFriendService friendService = new FriendService();
    private String currentSelectedFriend = null;
    private static final String RED_BORDER = "-fx-border-color: red;";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.initController(this.button, this));
        this.setEvents();
        Platform.runLater(() -> this.friends.getChildren().addAll(this.setFriendsToFriendPanel()));
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friendDisplayName -> friends.add(new Friend(friendDisplayName, false, (event -> this.openChat(friendDisplayName)))));
        return friends;
    }

    private void openChat(String friendDisplayName)
    {
        this.vBoxChats.getChildren().clear();
        this.currentSelectedFriend = friendDisplayName;
        this.scrollPaneChats.setContent(vBoxChats);
        /*{ //todo: get chats
            this.vBoxChats.getChildren().add(new Chat(friendDisplayName));
        }*/
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

    private void setEvents()
    {
        this.buttonSend.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.sendChat());
        this.vBoxChats.heightProperty().addListener((observable, oldValue, newValue) -> this.scrollPaneChats.setVvalue((Double)newValue));

        //todo: temp v
        this.restButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            this.userService.rest();
        });
        this.button.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            //FelixSession.getInstance().sendMessage("Hey!", "");
        });
    }

    private void sendChat()
    {
        this.textFieldMessage.setStyle(null);
        if (this.currentSelectedFriend == null)
        {
            return;
        }
        if (this.textFieldMessage.getText().isEmpty())
        {
            this.textFieldMessage.setStyle(RED_BORDER);
            return;
        }
        FelixSession.getInstance().sendMessage(this.textFieldMessage.getText(), this.currentSelectedFriend);
        this.vBoxChats.getChildren().add(new Chat(super.getUser().getDisplayName(), this.textFieldMessage.getText()));
        this.textFieldMessage.setText("");
    }

    @Override
    public void onMessage(WebSocketMessage webSocketMessage)
    {
        Platform.runLater(() -> this.vBoxChats.getChildren().add(new Chat(webSocketMessage.getFrom(), webSocketMessage.getMessage())));
    }
}