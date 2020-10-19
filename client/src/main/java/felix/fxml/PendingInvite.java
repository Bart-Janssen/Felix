package felix.fxml;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        Label name = new Label(this.pendingFriendDisplayName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(0, 0, 0, 10));
        Button cancelButton = new Button();
        cancelButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        cancelButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/cancel.png"))));
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        GridPane gridPane = new GridPane();
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(265);
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