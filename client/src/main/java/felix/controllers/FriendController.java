package felix.controllers;

import felix.exceptions.AlreadyLoggedInException;
import felix.fxml.Friend;
import felix.fxml.FriendInvite;
import felix.fxml.PendingInvite;
import felix.fxml.messageBox.CustomOkMessage;
import felix.main.FelixSession;
import felix.models.User;
import felix.models.WebSocketMessage;
import felix.service.friend.FriendService;
import felix.service.friend.IFriendService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FriendController extends MainController implements ILoginListener
{
    @FXML private Button buttonSendOutgoingInvite;
    @FXML private TextField textFieldDisplayNameOutgoingInvite;
    @FXML private VBox friends;
    @FXML private VBox friendInvites;
    @FXML private VBox pendingOutgoingInvites;
    @FXML private GridPane main;

    private IFriendService friendService = new FriendService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() ->
        {
            super.initController(this.main, this);
            this.friends.getChildren().addAll(this.setFriendsToFriendPanel());
            this.pendingOutgoingInvites.getChildren().addAll(this.getPendingOutgoingInvites());
            this.friendInvites.getChildren().addAll(this.getFriendInvites());
        });
        initializeEvents();
    }

    private List<FriendInvite> getFriendInvites()
    {
        try
        {
            List<FriendInvite> friendInvites = new ArrayList<>();
            this.friendService.getFriendInvites().forEach(friendInvite -> friendInvites.add(new FriendInvite(friendInvite.getDisplayName(), (event -> this.acceptInvite(friendInvite)), (event -> this.declineInvite(friendInvite)))));
            return friendInvites;
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting friend invites.").show();
            return new ArrayList<>();
        }
    }

    private void acceptInvite(User friendInvite)
    {
        try
        {
            this.friends.getChildren().add(new Friend(this.friendService.acceptInvite(friendInvite.getDisplayName()), friendInvite.isOnline(), true, (event -> this.removeFriend(friendInvite))));
            this.friendInvites.getChildren().removeIf(friend -> ((FriendInvite)friend).getFriendName().equals(friendInvite.getDisplayName()));
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while accepting friend invite.").show();
        }
    }

    private void declineInvite(User friendInvite)
    {
        try
        {
            this.friendService.declineInvite(friendInvite.getDisplayName());
            this.friendInvites.getChildren().removeIf(friend -> ((FriendInvite)friend).getFriendName().equals(friendInvite.getDisplayName()));
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while declining friend invite.").show();
        }
    }

    private List<PendingInvite> getPendingOutgoingInvites()
    {
        try
        {
            List<PendingInvite> pendingInvites = new ArrayList<>();
            this.friendService.getPendingOutgoingInvites().forEach(friendDisplayName -> pendingInvites.add(new PendingInvite(friendDisplayName, (event -> this.cancelInvite(friendDisplayName)))));
            return pendingInvites;
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting friend invites.").show();
            return new ArrayList<>();
        }
    }

    private void cancelInvite(String friendDisplayName)
    {
        try
        {
            this.friendService.cancelInvite(friendDisplayName);
            this.pendingOutgoingInvites.getChildren().removeIf(friendInvite -> ((PendingInvite)friendInvite).getFriendName().equals(friendDisplayName));
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while canceling friend invite.").show();
        }
    }

    private void initializeEvents()
    {
        this.friends.setSpacing(5);
        this.friendInvites.setSpacing(5);
        this.pendingOutgoingInvites.setSpacing(5);
        this.textFieldDisplayNameOutgoingInvite.addEventHandler(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode().equals(KeyCode.ENTER)) this.checkPendingOutgoingInvite();
        });
        this.buttonSendOutgoingInvite.setOnMouseClicked(event -> this.checkPendingOutgoingInvite());
    }

    private void checkPendingOutgoingInvite()
    {
        this.textFieldDisplayNameOutgoingInvite.setStyle(DEFAULT);
        if (this.textFieldDisplayNameOutgoingInvite.getText().isEmpty())
        {
            this.textFieldDisplayNameOutgoingInvite.setStyle(RED_BORDER);
            return;
        }
        this.sendPendingOutgoingInvite();
    }

    private void sendPendingOutgoingInvite()
    {
        if (this.friends.getChildren().stream().anyMatch(friend -> ((Friend)friend).getFriendName().equals(this.textFieldDisplayNameOutgoingInvite.getText())) || this.pendingOutgoingInvites.getChildren().stream().anyMatch(friend -> ((PendingInvite)friend).getFriendName().equals(this.textFieldDisplayNameOutgoingInvite.getText())))
        {
            new CustomOkMessage(stage, "This friend is already on your friend list.").show();
            this.textFieldDisplayNameOutgoingInvite.setText("");
            return;
        }
        if (super.getUser().getDisplayName().equals(this.textFieldDisplayNameOutgoingInvite.getText()))
        {
            new CustomOkMessage(stage, "you cannot send a friend invite to yourself.").show();
            this.textFieldDisplayNameOutgoingInvite.setText("");
            return;
        }
        try
        {
            User pendingFriend = this.friendService.sendPendingOutgoingInvite(this.textFieldDisplayNameOutgoingInvite.getText());
            this.pendingOutgoingInvites.getChildren().add(new PendingInvite(pendingFriend.getDisplayName(), (event -> this.cancelInvite(pendingFriend.getDisplayName()))));
            this.textFieldDisplayNameOutgoingInvite.setText("");
        }
        catch (Exception e)
        {
            this.textFieldDisplayNameOutgoingInvite.setText("");
            if (e instanceof AlreadyLoggedInException)
            {
                new CustomOkMessage(stage, "This friend is already on your friend list.").show();
                return;
            }
            new CustomOkMessage(stage, "This user might not exist.").show();
        }
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friend -> friends.add(new Friend(friend.getDisplayName(), friend.isOnline(), true, (event -> this.removeFriend(friend)))));
        return friends;
    }

    private void removeFriend(User friend)
    {
        try
        {
            this.friendService.remove(friend.getDisplayName());
            this.friends.getChildren().removeIf(currentFriend -> ((Friend)currentFriend).getFriendName().equals(friend.getDisplayName()));
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while removing friend.").show();
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