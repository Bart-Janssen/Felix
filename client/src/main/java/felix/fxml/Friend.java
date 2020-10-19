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

public class Friend extends Pane
{
    private String friendName;
    private Pane onlinePane;

    public Friend(String friendName, boolean online, boolean showRemoveButton, EventHandler<MouseEvent> acceptHandler)
    {
        this.friendName = friendName;
        final int WIDTH = 170;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0;");
        GridPane gridPane = new GridPane();
        Button removeButton = new Button();
        removeButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        removeButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/delete.png"))));
        if (!showRemoveButton) this.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        else removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        removeButton.setVisible(showRemoveButton);
        Label name = new Label(this.friendName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(0, 0, 0, 10));
        this.onlinePane = new Pane();
        this.onlinePane.setStyle(online ? "-fx-background-color:green;" : "-fx-background-color:red;");
        this.onlinePane.setPrefWidth(5);
        ColumnConstraints onlineColumn = new ColumnConstraints();
        onlineColumn.setPrefWidth(5);
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(showRemoveButton ? 120 : 160);
        ColumnConstraints buttonColumn = new ColumnConstraints();
        buttonColumn.setPrefWidth(65);
        gridPane.getColumnConstraints().addAll(onlineColumn, nameColumn, buttonColumn);
        gridPane.addColumn(0, this.onlinePane);
        gridPane.addColumn(1, name);
        gridPane.addColumn(2, removeButton);
        super.getChildren().addAll(gridPane);
    }

    public String getFriendName()
    {
        return this.friendName;
    }

    public void setOnline(boolean online)
    {
        this.onlinePane.setStyle(online ? "-fx-background-color:green;" : "-fx-background-color:red;");
    }
}