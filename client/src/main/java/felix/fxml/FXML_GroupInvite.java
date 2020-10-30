package felix.fxml;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.UUID;

public class FXML_GroupInvite extends Pane
{
    private UUID groupId;
    private TextField textFieldInviteName;

    public FXML_GroupInvite(UUID groupId, EventHandler<InputEvent> acceptHandler)
    {
        this.groupId = groupId;
        final int WIDTH = 310;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setMaxWidth(207);
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        Button inviteButton = new Button();
        inviteButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        inviteButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/invite.png"))));
        inviteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        inviteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> this.textFieldInviteName.setText(""));
        this.textFieldInviteName = new TextField();
        this.textFieldInviteName.setStyle("-fx-background-color: #707070; -fx-text-inner-color: #CCCCCC;");
        GridPane gridPane = new GridPane();
        ColumnConstraints inviteColumn = new ColumnConstraints();
        ColumnConstraints inviteNameColumn = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(inviteColumn, inviteNameColumn);
        gridPane.addColumn(0, inviteButton);
        gridPane.addColumn(1, textFieldInviteName);
        super.getChildren().addAll(gridPane);
    }

    public UUID getGroupId()
    {
        return this.groupId;
    }

    public String getInvitedUserDisplayName()
    {
        return this.textFieldInviteName.getText();
    }
}