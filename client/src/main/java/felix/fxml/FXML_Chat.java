package felix.fxml;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FXML_Chat extends VBox
{
    public FXML_Chat(String fromDisplayName, String message, Date date)
    {
        super.setPrefWidth(614);
        super.setMinHeight(45);
        Label from = new Label(fromDisplayName);
        from.setStyle("-fx-font-weight: bold;");
        Label dateLabel = new Label(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(date));
        dateLabel.setFont(new Font(10));
        dateLabel.setPadding(new Insets(2, 0, 0, 10));
        GridPane gridPane = new GridPane();
        GridPane.setMargin(from, new Insets(2, 0, 0, 10));
        ColumnConstraints fromColumn = new ColumnConstraints();
        ColumnConstraints dateColumn = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(fromColumn, dateColumn);
        gridPane.addColumn(0, from);
        gridPane.addColumn(1, dateLabel);
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(15));
        messageLabel.setMaxWidth(600);
        messageLabel.setPrefWidth(600);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(0, 0, 0, 10));
        super.setStyle("-fx-background-color: #909090; -fx-background-radius: 5 5 5 5; -fx-border-radius: 5 5 5 5;");
        super.getChildren().addAll(gridPane, messageLabel);
    }
}