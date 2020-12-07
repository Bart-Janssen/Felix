package felix.controllers;

import felix.fxml.FXML_Group;
import felix.fxml.FXML_GroupInvite;
import felix.fxml.FXML_GroupMember;
import felix.fxml.messageBox.CustomOkMessage;
import felix.models.Group;
import felix.service.group.GroupService;
import felix.service.group.IGroupService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class GroupController extends MainController
{
    @FXML private VBox invites;
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
            super.initController(this.main);
            this.groups.getChildren().addAll(this.setGroupsToPanel());
        });
        this.initializeEvents();
    }

    private void invite(UUID groupId)
    {
        FXML_GroupInvite invite = null;
        for (Node node : this.invites.getChildren())
        {
            if (((FXML_GroupInvite)node).getGroupId().equals(groupId))
            {
                invite = ((FXML_GroupInvite)node);
                break;
            }
        }
        if (invite == null)
        {
            new CustomOkMessage(stage, "You are not a member of this group.").show();
            return;
        }
        try
        {
            this.groupService.invite(groupId, invite.getInvitedUserDisplayName());
            new CustomOkMessage(stage, "Invite send!").show();
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "An error occurred when inviting " + invite.getInvitedUserDisplayName() + ".").show();
        }
    }

    private List<FXML_Group> setGroupsToPanel()
    {
        List<FXML_Group> groups = new ArrayList<>();
        this.getGroups().forEach(group ->
        {
            groups.add(new FXML_Group(group.getGroupName(), true, (event -> this.leaveGroup(group))));
            this.invites.getChildren().add(new FXML_GroupInvite(group.getId(), (event -> this.invite(group.getId()))));
        });
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
        this.invites.setSpacing(5);
        this.groups.setSpacing(5);
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
            this.groups.getChildren().add(new FXML_Group(newGroup.getGroupName(),true, (event -> this.leaveGroup(newGroup))));
            this.textFieldNewGroupName.setText("");
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Failed to make group.").show();
        }
    }
}