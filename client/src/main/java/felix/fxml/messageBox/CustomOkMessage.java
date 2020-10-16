package felix.fxml.messageBox;

import javafx.stage.Stage;
import java.util.Arrays;

public class CustomOkMessage extends CustomMessage
{
    public CustomOkMessage(Stage owner, String... messages)
    {
        super(owner, OK, Arrays.asList(messages));
    }

    @Override
    public FXML_MessageBoxStatus showAndAwaitStatus()
    {
        super.show();
        return super.getStatus();
    }

    @Override
    public void show()
    {
        super.show();
    }
}