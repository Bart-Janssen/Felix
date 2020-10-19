package felix.fxml;

import felix.fxml.messageBox.CustomOkMessage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class NavigationAnchor extends AnchorPane
{
    @FXML private Button buttonLogout;
    @FXML private Button buttonSwitchToProfile;
    @FXML private Button buttonSwitchToHome;
    @FXML private Button buttonSwitchToFriends;

    private static final String ON_EXIT = "-fx-background-color: #909090;";
    private static final String ON_ENTER = "-fx-background-color: #B0B0B0;";

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
            new CustomOkMessage(null, "Navigation bar cannot be loaded.").show();
        }
    }

    @FXML
    public void initialize()
    {
        buttonLogout.setOnMouseClicked(event -> onActionPropertyLogout().get().handle(event));
        buttonSwitchToProfile.setOnMouseClicked(event -> onActionPropertySwitchToProfile().get().handle(event));
        buttonSwitchToHome.setOnMouseClicked(event -> onActionPropertySwitchToHome().get().handle(event));
        buttonSwitchToFriends.setOnMouseClicked(event -> onActionPropertySwitchToFriends().get().handle(event));
        buttonLogout.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/logout.png"))));
        buttonSwitchToProfile.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/profile.png"))));
        buttonSwitchToHome.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/home.png"))));
        buttonSwitchToFriends.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("/friends.png"))));
        buttonLogout.setOnMouseExited(event -> buttonLogout.setStyle(ON_EXIT));
        buttonLogout.setOnMouseEntered(event -> buttonLogout.setStyle(ON_ENTER));
        buttonSwitchToProfile.setOnMouseExited(event -> buttonSwitchToProfile.setStyle(ON_EXIT));
        buttonSwitchToProfile.setOnMouseEntered(event -> buttonSwitchToProfile.setStyle(ON_ENTER));
        buttonSwitchToHome.setOnMouseExited(event -> buttonSwitchToHome.setStyle(ON_EXIT));
        buttonSwitchToHome.setOnMouseEntered(event -> buttonSwitchToHome.setStyle(ON_ENTER));
        buttonSwitchToFriends.setOnMouseExited(event -> buttonSwitchToFriends.setStyle(ON_EXIT));
        buttonSwitchToFriends.setOnMouseEntered(event -> buttonSwitchToFriends.setStyle(ON_ENTER));
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



    private ObjectProperty<EventHandler<MouseEvent>> propertyOnActionSwitchToHome = new SimpleObjectProperty<>();

    private ObjectProperty<EventHandler<MouseEvent>> onActionPropertySwitchToHome()
    {
        return propertyOnActionSwitchToHome;
    }

    public final void setOnActionSwitchToHome(EventHandler<MouseEvent> handler)
    {
        propertyOnActionSwitchToHome.set(handler);
    }

    public final EventHandler<MouseEvent> getOnActionSwitchToHome()
    {
        return propertyOnActionSwitchToHome.get();
    }





    private ObjectProperty<EventHandler<MouseEvent>> propertyOnActionSwitchToFriends = new SimpleObjectProperty<>();

    private ObjectProperty<EventHandler<MouseEvent>> onActionPropertySwitchToFriends()
    {
        return propertyOnActionSwitchToFriends;
    }

    public final void setOnActionSwitchToFriends(EventHandler<MouseEvent> handler)
    {
        propertyOnActionSwitchToFriends.set(handler);
    }

    public final EventHandler<MouseEvent> getOnActionSwitchToFriends()
    {
        return propertyOnActionSwitchToFriends.get();
    }
}