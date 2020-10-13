package felix.controllers;

import felix.exceptions.AlreadyLoggedInException;
import felix.fxml.Friend;
import felix.fxml.FriendInvite;
import felix.fxml.PendingInvite;
import felix.models.User;
import felix.service.friend.FriendService;
import felix.service.friend.IFriendService;
import javafx.application.Platform;
import javafx.fxml.FXML;
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

public class FriendController extends MainController
{
    @FXML private Button buttonSendOutgoingInvite;
    @FXML private TextField textFieldDisplayNameOutgoingInvite;
    @FXML private VBox friends;
    @FXML private VBox friendInvites;
    @FXML private VBox pendingOutgoingInvites;
    @FXML private GridPane main;

    private IFriendService friendService = new FriendService();

    private static final String RED_BORDER = "-fx-border-color: red;";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() ->
        {
            super.setStage(this.main);
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
            this.friendService.getFriendInvites().forEach(friendDisplayName -> friendInvites.add(new FriendInvite(friendDisplayName, (event -> this.acceptInvite(friendDisplayName)), (event -> this.declineInvite(friendDisplayName)))));
            return friendInvites;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void acceptInvite(String friendDisplayName)
    {
        try
        {
            this.friends.getChildren().add(new Friend(this.friendService.acceptInvite(friendDisplayName), true, (event -> this.removeFriend(friendDisplayName))));
            this.friendInvites.getChildren().removeIf(friend -> ((FriendInvite)friend).getFriendName().equals(friendDisplayName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void declineInvite(String friendDisplayName)
    {
        try
        {
            this.friendService.declineInvite(friendDisplayName);
            this.friendInvites.getChildren().removeIf(friend -> ((FriendInvite)friend).getFriendName().equals(friendDisplayName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private void initializeEvents()
    {
        this.textFieldDisplayNameOutgoingInvite.addEventHandler(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode().equals(KeyCode.ENTER)) this.checkPendingOutgoingInvite();
        });
        this.buttonSendOutgoingInvite.setOnMouseClicked(event -> this.checkPendingOutgoingInvite());
    }

    private void checkPendingOutgoingInvite()
    {
        this.textFieldDisplayNameOutgoingInvite.setStyle(null);
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
            System.out.println("friend alrdy exists");
            this.textFieldDisplayNameOutgoingInvite.setText("");
            return; //todo msg friend alrdy exists
        }
        try
        {
            User pendingFriend = this.friendService.sendPendingOutgoingInvite(this.textFieldDisplayNameOutgoingInvite.getText());
            this.pendingOutgoingInvites.getChildren().add(new PendingInvite(pendingFriend.getDisplayName(), (event -> this.cancelInvite(this.textFieldDisplayNameOutgoingInvite.getText()))));
            this.textFieldDisplayNameOutgoingInvite.setText("");
        }
        catch (Exception e)
        {
            this.textFieldDisplayNameOutgoingInvite.setText("");
            if (e instanceof AlreadyLoggedInException)
            {
                System.out.println("friend alrdy exists");
                //todo msg friend alrdy exists
                return;
            }
            System.out.println("This user might not exist.");
            //todo msgbox
            e.printStackTrace();
        }
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friendDisplayName -> friends.add(new Friend(friendDisplayName, true, (event -> this.removeFriend(friendDisplayName)))));
        return friends;
    }

    private void removeFriend(String friendDisplayName)
    {
        try
        {
            this.friendService.remove(friendDisplayName);
            this.friends.getChildren().removeIf(friend -> ((Friend)friend).getFriendName().equals(friendDisplayName));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
}