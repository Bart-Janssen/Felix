package felix.fxml.messageBox;

import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class CustomErrorMessage extends CustomMessage
{
    public CustomErrorMessage(Stage owner)
    {
        super(owner, ERROR, new ArrayList<>());
        List<String> messages = new ArrayList<>();
        messages.add("\n");
        messages.add("Noteer dit bovenstaande en vertel wat er gebeurde."); //todo error msg
        messages.add("Tip: Maak foto of printscreen.");
        super.setMessages(messages);
    }

    public CustomErrorMessage()
    {
        super(null, ERROR, new ArrayList<>());
        List<String> messages = new ArrayList<>();
        messages.add("\n");
        messages.add("Noteer dit bovenstaande en vertel wat er gebeurde.");
        messages.add("Tip: Maak foto of printscreen.");
        super.setMessages(messages);
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