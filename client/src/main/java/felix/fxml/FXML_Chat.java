package felix.fxml;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class FXML_Chat extends VBox
{
    public FXML_Chat(String fromDisplayName, String message)
    {
        super.setPrefWidth(614);
        super.setMinHeight(45);
        Label from = new Label(fromDisplayName);
        from.setStyle("-fx-font-weight: bold;");
        from.setPadding(new Insets(2, 0, 0, 10));
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(15));
        messageLabel.setMaxWidth(600);
        messageLabel.setPrefWidth(600);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(0, 0, 0, 10));
        super.setStyle("-fx-background-color:green; -fx-border-color:blue; -fx-border-width: 1;");
        /*new Thread(() ->
        {
            for (;;)
            {
                if (messageLabel.getHeight() > 0.0D)
                {
                    System.out.println(super.getHeight());
                    super.setHeight(messageLabel.getHeight() + from.getHeight());
                    break;
                }
            }
        }).start();*/
        super.getChildren().addAll(from, messageLabel);
    }
}