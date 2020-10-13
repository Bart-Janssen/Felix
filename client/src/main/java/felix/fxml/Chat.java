package felix.fxml;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class Chat extends Pane
{
    public Chat(String displayName)
    {
        super.setPrefWidth(100);
        super.setPrefHeight(40);
        super.setStyle("-fx-background-color:green; -fx-border-color:blue; -fx-border-width: 1;");
        Label from = new Label(displayName);
        from.setStyle("-fx-font-weight: bold;");
        from.setPadding(new Insets(2, 0, 0, 10));


        Label name = new Label("test chat, static text btw");
        name.setFont(new Font(20));
        name.setPadding(new Insets(15, 0, 0, 10));
//        name.layoutYProperty().bind(super.heightProperty().subtract(name.heightProperty()).divide(2));
        super.getChildren().addAll(name, from);
    }
}