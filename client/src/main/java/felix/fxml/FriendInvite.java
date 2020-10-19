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

public class FriendInvite extends Pane
{
    private Button acceptButton;
    private Button declineButton;
    private String pendingFriendDisplayName;

    public FriendInvite(String pendingFriendDisplayName, EventHandler<MouseEvent> acceptHandler, EventHandler<MouseEvent> declineHandler)
    {
        this.pendingFriendDisplayName = pendingFriendDisplayName;
        final int WIDTH = 310;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        GridPane gridPane = new GridPane();
        this.acceptButton = new Button();
        this.acceptButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        this.acceptButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/accept.png"))));
        this.declineButton = new Button();
        this.declineButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        this.declineButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/cancel.png"))));
        this.acceptButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        this.declineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, declineHandler);
        this.acceptButton.setPadding(new Insets(0, 5, 0, 0));
//        GridPane.setMargin(this.acceptButton, new Insets(0, 5, 0, 0));
//        GridPane.setMargin(this.declineButton, new Insets(4, 0, 0, 0));
        Label name = new Label(this.pendingFriendDisplayName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(0, 0, 0, 10));
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(230);
        ColumnConstraints buttonAccept = new ColumnConstraints();
        buttonAccept.setPrefWidth(30);
        ColumnConstraints buttonDecline = new ColumnConstraints();
        buttonDecline.setPrefWidth(30);
        gridPane.getColumnConstraints().addAll(nameColumn, buttonAccept, buttonDecline);
        gridPane.addColumn(0, name);
        gridPane.addColumn(1, this.acceptButton);
        gridPane.addColumn(2, this.declineButton);
        super.getChildren().addAll(gridPane);
    }

    public Button getAcceptButton()
    {
        return this.acceptButton;
    }

    public Button getDeclineButton()
    {
        return this.declineButton;
    }

    public String getFriendName()
    {
        return this.pendingFriendDisplayName;
    }
}