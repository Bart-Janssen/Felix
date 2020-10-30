package felix.controllers;

import felix.fxml.FXML_Group;
import felix.fxml.messageBox.CustomOkMessage;
import felix.models.Group;
import felix.models.WebSocketMessage;
import felix.service.group.GroupService;
import felix.service.group.IGroupService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GroupController extends MainController implements ILoginListener
{
    @FXML private Button buttonCreateNewGroup;
    @FXML private TextField textFieldNewGroupName;
    @FXML private GridPane main;
    @FXML private VBox groups;

    private IGroupService groupService = new GroupService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() ->
        {
            super.initController(this.main, this);
            this.groups.getChildren().addAll(this.setGroupsToPanel());
        });
        this.initializeEvents();
    }

    private List<FXML_Group> setGroupsToPanel()
    {
        List<FXML_Group> groups = new ArrayList<>();
        this.getGroups().forEach(group -> groups.add(new FXML_Group(group.getGroupName(), (event -> this.leaveGroup(group)))));
        return groups;
    }

    private void leaveGroup(Group group)
    {
        try
        {
            this.groupService.leaveGroup(group);
            this.groups.getChildren().removeIf(memberGroup -> ((FXML_Group)memberGroup).getGroupName().equals(group.getGroupName()));
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while leaving group.").show();
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

    private void initializeEvents()
    {
        this.buttonCreateNewGroup.setOnMouseClicked(event -> this.createNewGroup());
    }

    private void createNewGroup()
    {
        this.textFieldNewGroupName.setStyle(DEFAULT);
        if (this.textFieldNewGroupName.getText().length() == 0)
        {
            this.textFieldNewGroupName.setStyle(RED_BORDER);
            new CustomOkMessage(stage, "The group name may not be empty.").show();
            return;
        }
        if (this.textFieldNewGroupName.getText().length() > 12)
        {
            this.textFieldNewGroupName.setStyle(RED_BORDER);
            new CustomOkMessage(stage, "The group name may not be longer then 12 characters.").show();
            return;
        }
        try
        {
            Group newGroup = this.groupService.createGroup(this.textFieldNewGroupName.getText());
            this.groups.getChildren().add(new FXML_Group(newGroup.getGroupName(), (event -> this.leaveGroup(newGroup))));
            this.textFieldNewGroupName.setText("");
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Failed to make group.").show();
        }
    }

    @Override
    public void onLogin(WebSocketMessage webSocketMessage)
    {

    }

    @Override
    public void onLogout(WebSocketMessage webSocketMessage)
    {

    }
}