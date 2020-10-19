package felix.fxml.messageBox;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

abstract class FXML_MessageBoxBase implements Initializable
{
    @FXML
    private GridPane mainGrid;

    VBox vBox = new VBox();
    private ScrollPane scrollPane = new ScrollPane();

    private FXML_MessageBoxStatus status = FXML_MessageBoxStatus.CANCEL;

    void initData(List<String> messages)
    {
        Platform.runLater(() ->
        {
            for (String message : messages)
            {
                Label label = new Label(message);
                label.setWrapText(true);
                label.setStyle("-fx-padding: 0px 0px 0px 3px;");
                vBox.getChildren().add(label);
            }
        });
    }

    FXML_MessageBoxStatus getStatus()
    {
        return status;
    }

    void initialize()
    {
        vBox.setStyle("-fx-background-color:#707070;");
        scrollPane.setStyle("-fx-background:#707070;");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBox);
        mainGrid.add(scrollPane,0,0);
    }

    void close(FXML_MessageBoxStatus status)
    {
        this.status = status;
        ((Stage)vBox.getScene().getWindow()).close();
    }
}