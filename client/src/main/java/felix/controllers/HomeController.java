package felix.controllers;

import felix.fxml.FXML_Chat;
import felix.fxml.Friend;
import felix.fxml.messageBox.CustomOkMessage;
import felix.main.FelixSession;
import felix.models.User;
import felix.models.WebSocketMessage;
import felix.service.chat.ChatService;
import felix.service.chat.IChatService;
import felix.service.friend.FriendService;
import felix.service.friend.IFriendService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends MainController implements IMessageListener, ILoginListener
{
    @FXML private ScrollPane scrollPaneChats;
    @FXML private TextField textFieldMessage;
    @FXML private Button buttonSend;
    @FXML private VBox friends;
    private VBox vBoxChats = new VBox(5);
    private IFriendService friendService = new FriendService();
    private String currentSelectedFriend = null;
    private static final String RED_BORDER = "-fx-border-color: red;";

    private IChatService chatService = new ChatService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.buttonSend.setDisable(true);
        this.textFieldMessage.setDisable(true);
        Platform.runLater(() -> super.initController(this.buttonSend, this));
        this.setEvents();
        Platform.runLater(() -> this.friends.getChildren().addAll(this.setFriendsToFriendPanel()));
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friend -> friends.add(new Friend(friend.getDisplayName(), friend.isOnline(),false, (event -> this.openChat(friend.getDisplayName())))));
        return friends;
    }

    private void openChat(String friendDisplayName)
    {
        this.textFieldMessage.setPromptText("Type your message to " + friendDisplayName);
        this.buttonSend.setDisable(false);
        this.textFieldMessage.setDisable(false);
        this.vBoxChats.getChildren().clear();
        this.currentSelectedFriend = friendDisplayName;
        this.scrollPaneChats.setContent(this.vBoxChats);
        try
        {
            List<FXML_Chat> chats = new ArrayList<>();
            this.chatService.getAll(friendDisplayName).forEach(friend -> chats.add(new FXML_Chat(friendDisplayName, friend)));
            this.vBoxChats.getChildren().addAll(chats);
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting chats.").show();
        }
    }

    private List<User> getFriends()
    {
        try
        {
            return this.friendService.getFriends();
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting friends.").show();
            return new ArrayList<>();
        }
    }

    private void setEvents()
    {
        if (this.currentSelectedFriend == null) this.textFieldMessage.setPromptText("Select a chat to begin chatting");
        this.friends.setSpacing(5);
        this.buttonSend.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.sendChat());
        this.textFieldMessage.addEventHandler(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode().equals(KeyCode.ENTER)) this.sendChat();
        });
        this.vBoxChats.heightProperty().addListener((observable, oldValue, newValue) -> this.scrollPaneChats.setVvalue((Double)newValue));
    }

    private void sendChat()
    {
        try
        {
            this.textFieldMessage.setStyle(null);
            if (this.currentSelectedFriend == null) return;
            if (this.textFieldMessage.getText().isEmpty())
            {
                this.textFieldMessage.setStyle(RED_BORDER);
                return;
            }
            if (this.textFieldMessage.getText().length() > 255)
            {
                new CustomOkMessage(stage, "Messages may not be bigger than 255 characters.").show();
                return;
            }
            FelixSession.getInstance().sendMessage(this.textFieldMessage.getText(), this.currentSelectedFriend);
            this.vBoxChats.getChildren().add(new FXML_Chat(super.getUser().getDisplayName(), this.textFieldMessage.getText()));
            this.textFieldMessage.setText("");
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Encryption error, disconnecting...").show();
        }
    }

    @Override
    public void onMessage(WebSocketMessage webSocketMessage)
    {
        Platform.runLater(() -> this.vBoxChats.getChildren().add(new FXML_Chat(webSocketMessage.getFrom(), webSocketMessage.getMessage())));
    }

    @Override
    public void onLogin(WebSocketMessage webSocketMessage)
    {
        this.friends.getChildren().stream().filter(friend -> ((Friend)friend).getFriendName().equals(webSocketMessage.getFrom())).findFirst().ifPresent(friend -> ((Friend) friend).setOnline(true));
    }

    @Override
    public void onLogout(WebSocketMessage webSocketMessage)
    {
        this.friends.getChildren().stream().filter(friend -> ((Friend)friend).getFriendName().equals(webSocketMessage.getFrom())).findFirst().ifPresent(friend -> ((Friend) friend).setOnline(false));
    }
}