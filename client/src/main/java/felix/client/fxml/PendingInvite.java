package felix.client.fxml;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class PendingInvite extends Pane
{
    private String pendingFriendDisplayName;

    public PendingInvite(String pendingFriendDisplayName, EventHandler<MouseEvent> acceptHandler)
    {
        this.pendingFriendDisplayName = pendingFriendDisplayName;
        final int WIDTH = 310;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color:green; -fx-border-color:blue; -fx-border-width: 1;");
        Label name = new Label(this.pendingFriendDisplayName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(5, 0, 0, 10));
        Button cancelButton = new Button("Cancel");
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        GridPane gridPane = new GridPane();
        GridPane.setMargin(cancelButton, new Insets(4, 0, 0, 0));
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(186);
        ColumnConstraints buttonColumn = new ColumnConstraints();
        buttonColumn.setPrefWidth(124);
        gridPane.getColumnConstraints().addAll(nameColumn, buttonColumn);
        gridPane.addColumn(0, name);
        gridPane.addColumn(1, cancelButton);
        super.getChildren().addAll(gridPane);
    }

    public String getFriendName()
    {
        return this.pendingFriendDisplayName;
    }
}