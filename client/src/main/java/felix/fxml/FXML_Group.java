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

public class FXML_Group extends Pane
{
    private String groupName;

    public FXML_Group(String groupName, boolean showLeaveButton, EventHandler<MouseEvent> acceptHandler)
    {
        this.groupName = groupName;
        final int WIDTH = 170;
        super.setPrefWidth(WIDTH);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 0 5 5 0; -fx-border-radius: 0 5 5 0;");
        GridPane gridPane = new GridPane();
        Button leaveButton = new Button();
        leaveButton.setStyle("-fx-border-width: 1; -fx-background-radius: 0; -fx-border-color: transparent; -fx-background-color: transparent;");
        leaveButton.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/delete.png"))));
        if (!showLeaveButton) this.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        else leaveButton.addEventHandler(MouseEvent.MOUSE_CLICKED, acceptHandler);
        leaveButton.setVisible(showLeaveButton);
        Label name = new Label(this.groupName);
        name.setFont(new Font(20));
        name.setPadding(new Insets(showLeaveButton ? 0 : 5, 0, 0, 10));
        if (showLeaveButton)
        {
            ColumnConstraints buttonColumn = new ColumnConstraints();
            buttonColumn.setPrefWidth(30);
            gridPane.getColumnConstraints().add(buttonColumn);
            gridPane.addColumn(0, leaveButton);
        }
        ColumnConstraints nameColumn = new ColumnConstraints();
        nameColumn.setPrefWidth(125);
        gridPane.getColumnConstraints().add(nameColumn);
        gridPane.addColumn(showLeaveButton ? 1 : 0, name);
        super.getChildren().addAll(gridPane);
    }

    public String getGroupName()
    {
        return this.groupName;
    }
}