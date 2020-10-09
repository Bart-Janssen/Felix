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

public class Friend extends Pane
{
    private String friendName;

    public Friend(String friendName, boolean showRemoveButton, EventHandler<MouseEvent> acceptHandler)
    {
        this.friendName = friendName;
        final int WIDTH = 170;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color:green; -fx-border-color:blue; -fx-border-width: 1;");
        GridPane gridPane = new GridPane();
        Button removeButton = new Button("Remove");
        if (!showRemoveButton) this.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        else removeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        removeButton.setVisible(showRemoveButton);
        GridPane.setMargin(removeButton, new Insets(4, 0, 0, 0));
        Label name = new Label(this.friendName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(5, 0, 0, 10));
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(105);
        ColumnConstraints buttonColumn = new ColumnConstraints();
        buttonColumn.setPrefWidth(65);
        gridPane.getColumnConstraints().addAll(nameColumn, buttonColumn);
        gridPane.addColumn(0, name);
        gridPane.addColumn(1, removeButton);
        super.getChildren().addAll(gridPane);
    }

    public String getFriendName()
    {
        return this.friendName;
    }
}