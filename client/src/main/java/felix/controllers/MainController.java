package felix.controllers;

import felix.fxml.NavigationAnchor;
import felix.fxml.messageBox.CustomOkMessage;
import felix.main.Felix;
import felix.main.FelixSession;
import felix.models.User;
import felix.models.View;
import felix.models.WebSocketMessage;
import felix.service.EncryptionManager;
import felix.service.system.JwtDecoder;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

abstract class MainController extends EncryptionManager implements Initializable
{
    @FXML protected NavigationAnchor navigationAnchor;
    @FXML static Stage stage;
    private static IListener controller;

    static final String DEFAULT = "-fx-text-inner-color: #CCCCCC; -fx-background-color: #707070;";
    static final String RED_BORDER = "-fx-border-color: #FF0000;" + DEFAULT;

    private IUserService userService = new UserService();

    void initController(Node currentView)
    {
        stage = (Stage)currentView.getScene().getWindow();
        stage.setOnCloseRequest(event ->
        {
            if (!(this instanceof AuthenticationController)) this.logout();
            FelixSession.getInstance().disconnect();
            System.exit(0);
        });
    }

    void initController(Node currentView, IListener controller)
    {
        MainController.controller = controller;
        stage = (Stage)currentView.getScene().getWindow();
        stage.setOnCloseRequest(event ->
        {
            if (!(this instanceof AuthenticationController)) this.logout();
            FelixSession.getInstance().disconnect();
            System.exit(0);
        });
    }

    void handleMessage(WebSocketMessage webSocketMessage)
    {
        switch (webSocketMessage.getType())
        {
            case MESSAGE:
                if (MainController.controller instanceof IMessageListener) ((IMessageListener)MainController.controller).onMessage(webSocketMessage);
                break;
            case LOGIN:
                if (MainController.controller instanceof ILoginListener) ((ILoginListener)MainController.controller).onLogin(webSocketMessage);
                break;
            case LOGOUT:
                if (MainController.controller instanceof ILoginListener) ((ILoginListener)MainController.controller).onLogout(webSocketMessage);
                break;
            default:
                break;
        }
    }

    void openNewView(final View view)
    {
        Platform.runLater(() ->
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(Felix.class.getResource("/view/" + view.getPage() + ".fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/logo.png")));
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/custom/navstyle.css").toExternalForm());
                stage.setTitle("Felix - " + view.getPage().substring(view.getPage().indexOf("/") + 1).substring(0, 1).toUpperCase() + view.getPage().substring(view.getPage().indexOf("/") + 2));
                stage.show();
            }
            catch (Exception e)
            {
                if (view.equals(View.PAGE_NOT_FOUND)) return;
                openNewView(View.PAGE_NOT_FOUND);
            }
        });
    }

    @FXML
    private void logout()
    {
        try
        {
            this.userService.logout();
            FelixSession.getInstance().setToken(null);
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "An error occurred.").show();
        }
        this.openNewView(View.LOGIN);
    }

    @FXML
    public void switchToProfile()
    {
        this.openNewView(View.PROFILE);
    }

    @FXML
    public void switchToHome()
    {
        this.openNewView(View.HOME);
    }

    @FXML
    public void switchToFriends()
    {
        this.openNewView(View.FRIENDS);
    }

    protected User getUser()
    {
        return new JwtDecoder().decode(FelixSession.getInstance().getToken());
    }
}