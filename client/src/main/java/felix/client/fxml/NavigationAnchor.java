package felix.client.fxml;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class NavigationAnchor extends AnchorPane
{
    @FXML private Button buttonLogout;

    public NavigationAnchor()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/custom/navigationAnchor.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize()
    {
        buttonLogout.setOnMouseClicked(event -> onActionPropertySwitchToTellen().get().handle(event));
    }

    private ObjectProperty<EventHandler<MouseEvent>> propertyOnActionSwitchToTellen = new SimpleObjectProperty<>();

    private ObjectProperty<EventHandler<MouseEvent>> onActionPropertySwitchToTellen()
    {
        return propertyOnActionSwitchToTellen;
    }

    public final void setOnActionSwitchToTellen(EventHandler<MouseEvent> handler)
    {
        propertyOnActionSwitchToTellen.set(handler);
    }

    public final EventHandler<MouseEvent> getOnActionSwitchToTellen()
    {
        return propertyOnActionSwitchToTellen.get();
    }
}