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
    @FXML private Button buttonSwitchToProfile;

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
        buttonLogout.setOnMouseClicked(event -> onActionPropertyLogout().get().handle(event));
        buttonSwitchToProfile.setOnMouseClicked(event -> onActionPropertySwitchToProfile().get().handle(event));
    }

    private ObjectProperty<EventHandler<MouseEvent>> propertyOnActionLogout = new SimpleObjectProperty<>();

    private ObjectProperty<EventHandler<MouseEvent>> onActionPropertyLogout()
    {
        return propertyOnActionLogout;
    }

    public final void setOnActionLogout(EventHandler<MouseEvent> handler)
    {
        propertyOnActionLogout.set(handler);
    }

    public final EventHandler<MouseEvent> getOnActionLogout()
    {
        return propertyOnActionLogout.get();
    }

    private ObjectProperty<EventHandler<MouseEvent>> propertyOnActionSwitchToProfile = new SimpleObjectProperty<>();

    private ObjectProperty<EventHandler<MouseEvent>> onActionPropertySwitchToProfile()
    {
        return propertyOnActionSwitchToProfile;
    }

    public final void setOnActionSwitchToProfile(EventHandler<MouseEvent> handler)
    {
        propertyOnActionSwitchToProfile.set(handler);
    }

    public final EventHandler<MouseEvent> getOnActionSwitchToProfile()
    {
        return propertyOnActionSwitchToProfile.get();
    }
}