package felix.controllers;

import felix.fxml.FXML_Chat;
import felix.fxml.FXML_Group;
import felix.fxml.FXML_GroupMember;
import felix.fxml.Friend;
import felix.fxml.messageBox.CustomOkMessage;
import felix.main.FelixSession;
import felix.models.Group;
import felix.models.User;
import felix.models.WebSocketMessage;
import felix.service.chat.ChatService;
import felix.service.chat.IChatService;
import felix.service.friend.FriendService;
import felix.service.friend.IFriendService;
import felix.service.group.GroupService;
import felix.service.group.IGroupService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.*;

public class HomeController extends MainController implements IMessageListener, ILoginListener
{
    @FXML private Pane paneFriends;
    @FXML private Pane paneGroups;
    @FXML private ScrollPane scrollPaneChats;
    @FXML private TextField textFieldMessage;
    @FXML private Button buttonSend;
    @FXML private VBox vBoxFriendsAndGroups;
    private VBox vBoxChats = new VBox(5);
    private IFriendService friendService = new FriendService();
    private IChatService chatService = new ChatService();
    private IGroupService groupService = new GroupService();
    private String chatTarget = null;
    private boolean isGroupChat;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.buttonSend.setDisable(true);
        this.textFieldMessage.setDisable(true);
        buttonSend.setStyle("-fx-background-color: #606060; -fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent;");
        buttonSend.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/send.png"))));
        Platform.runLater(() -> super.initController(this.buttonSend, this));
        this.setEvents();
        this.openFriends();
    }

    private List<Friend> setFriendsToFriendPanel()
    {
        List<Friend> friends = new ArrayList<>();
        this.getFriends().forEach(friend -> friends.add(new Friend(friend.getDisplayName(), friend.isOnline(),false, (event -> this.openFriendChat(friend.getDisplayName())))));
        return friends;
    }

    private List<FXML_Group> setGroupsToFriendPanel()
    {
        List<FXML_Group> groups = new ArrayList<>();
        this.getGroups().forEach(group ->
        {
            List<FXML_GroupMember> members = new ArrayList<>();
            group.getGroupMembers().forEach(member -> members.add(new FXML_GroupMember(member.getDisplayName().equals(group.getOwnerDisplayName()), false, group.getGroupName(), member.getDisplayName(), member.isOnline())));
            this.vBoxFriendsAndGroups.getChildren().addAll(members);
            groups.add(new FXML_Group(group.getGroupName(), false, (event -> this.openGroupChat(group))));
        });
        return groups;
    }

    private void openGroupChat(Group group)
    {
        List<Node> groupMembers = new ArrayList<>();
        this.textFieldMessage.setPromptText("Type your message in " + group.getGroupName());
        this.buttonSend.setDisable(false);
        this.textFieldMessage.setDisable(false);
        this.vBoxChats.getChildren().clear();
        this.chatTarget = group.getId().toString();
        this.scrollPaneChats.setContent(this.vBoxChats);
        for (Node node : this.vBoxFriendsAndGroups.getChildren())
        {
            if (node instanceof FXML_GroupMember)
            {
                if (((FXML_GroupMember)node).getGroupParent().equals(group.getGroupName())) groupMembers.add(node);
            }
        }
        this.vBoxFriendsAndGroups.getChildren().clear();
        this.vBoxFriendsAndGroups.getChildren().add(new FXML_Group(group.getGroupName(), false, (event -> this.openGroupChat(group))));
        groupMembers.forEach(node -> ((FXML_GroupMember)node).setVisible());
        this.vBoxFriendsAndGroups.getChildren().addAll(groupMembers);
        try
        {
            List<FXML_Chat> chats = new ArrayList<>();
            this.chatService.getAllGroup(group.getId()).forEach(chat -> chats.add(new FXML_Chat(chat.getDisplayNameFrom(), chat.getMessage(), new Date(chat.getDate()))));
            this.vBoxChats.getChildren().addAll(chats);
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting chats.").show();
        }
    }

    private void openFriendChat(String friendDisplayName)
    {
        this.textFieldMessage.setPromptText("Type your message to " + friendDisplayName);
        this.buttonSend.setDisable(false);
        this.textFieldMessage.setDisable(false);
        this.vBoxChats.getChildren().clear();
        this.chatTarget = friendDisplayName;
        this.scrollPaneChats.setContent(this.vBoxChats);
        try
        {
            List<FXML_Chat> chats = new ArrayList<>();
            this.chatService.getAll(friendDisplayName).forEach(chat -> chats.add(new FXML_Chat(chat.getDisplayNameFrom(), chat.getMessage(), new Date(chat.getDate()))));
            this.vBoxChats.getChildren().addAll(chats);
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting chats.").show();
        }
    }

    private List<Group> getGroups()
    {
        try
        {
            return this.groupService.getGroups();
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while getting groups.").show();
            return new ArrayList<>();
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
        if (this.chatTarget == null) this.textFieldMessage.setPromptText("Select a chat or group to begin chatting");
        this.vBoxFriendsAndGroups.setSpacing(5);
        this.buttonSend.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.sendChat());
        this.textFieldMessage.addEventHandler(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode().equals(KeyCode.ENTER)) this.sendChat();
        });
        this.vBoxChats.heightProperty().addListener((observable, oldValue, newValue) -> this.scrollPaneChats.setVvalue((Double)newValue));
        this.paneFriends.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.openFriends());
        this.paneGroups.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> this.openGroups());
        this.paneFriends.setCursor(Cursor.HAND);
        this.paneGroups.setCursor(Cursor.HAND);
    }

    private void openFriends()
    {
        this.vBoxChats.getChildren().clear();
        this.isGroupChat = false;
        this.textFieldMessage.setPromptText("Select a chat or group to begin chatting");
        this.vBoxFriendsAndGroups.getChildren().clear();
        this.vBoxFriendsAndGroups.getChildren().addAll(this.setFriendsToFriendPanel());
        this.paneFriends.setStyle("-fx-background-color: #707070; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        this.paneGroups.setStyle("-fx-background-color: #606060; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
    }

    private void openGroups()
    {
        this.vBoxChats.getChildren().clear();
        this.isGroupChat = true;
        this.textFieldMessage.setPromptText("Select a chat or group to begin chatting");
        this.vBoxFriendsAndGroups.getChildren().clear();
        this.vBoxFriendsAndGroups.getChildren().addAll(this.setGroupsToFriendPanel());
        this.paneGroups.setStyle("-fx-background-color: #707070; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        this.paneFriends.setStyle("-fx-background-color: #606060; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
    }

    private void sendChat()
    {
        try
        {
            this.textFieldMessage.setStyle(DEFAULT);
            if (this.chatTarget == null) return;
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
            FelixSession.getInstance().sendMessage(this.textFieldMessage.getText(), this.isGroupChat, this.chatTarget);
            this.vBoxChats.getChildren().add(new FXML_Chat(super.getUser().getDisplayName(), this.textFieldMessage.getText(), new Date()));
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
        Platform.runLater(() -> this.vBoxChats.getChildren().add(new FXML_Chat(webSocketMessage.getFrom(), webSocketMessage.getMessage(), new Date())));
    }

    @Override
    public void onLogin(WebSocketMessage webSocketMessage)
    {
        if (this.isGroupChat)
        {
            for (Node item : vBoxFriendsAndGroups.getChildren())
            {
                if ((item instanceof FXML_GroupMember))
                {
                    if (((FXML_GroupMember)item).getMemberName().equals(webSocketMessage.getFrom())) ((FXML_GroupMember)item).setOnline(true);
                }
            }
        }
        else
        {
            this.vBoxFriendsAndGroups.getChildren().stream().filter(friend -> ((Friend)friend).getFriendName().equals(webSocketMessage.getFrom())).findFirst().ifPresent(friend -> ((Friend) friend).setOnline(true));
        }
    }

    @Override
    public void onLogout(WebSocketMessage webSocketMessage)
    {
        if (this.isGroupChat)
        {
            for (Node item : vBoxFriendsAndGroups.getChildren())
            {
                if ((item instanceof FXML_GroupMember))
                {
                    if (((FXML_GroupMember)item).getMemberName().equals(webSocketMessage.getFrom())) ((FXML_GroupMember)item).setOnline(false);
                }
            }
        }
        else
        {
            this.vBoxFriendsAndGroups.getChildren().stream().filter(friend -> ((Friend)friend).getFriendName().equals(webSocketMessage.getFrom())).findFirst().ifPresent(friend -> ((Friend) friend).setOnline(false));
        }
    }
}