package felix.fxml.messageBox;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class FXML_MessageBoxOk extends FXML_MessageBoxBase
{
    @FXML
    private Button buttonOk;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        super.initialize();
        addButtonOkEvents();
    }

    private void addButtonOkEvents()
    {
        buttonOk.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> super.close(FXML_MessageBoxStatus.CONTINUE));
        buttonOk.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode() == KeyCode.ENTER || key.getCode() == KeyCode.SPACE) super.close(FXML_MessageBoxStatus.CONTINUE);
        });
    }
}