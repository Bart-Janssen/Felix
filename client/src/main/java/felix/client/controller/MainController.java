package felix.client.controller;

import felix.client.fxml.NavigationAnchor;
import felix.client.main.FelixSession;
import felix.client.models.View;
import felix.client.service.EncryptionManager;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import felix.client.main.Main;

abstract class MainController extends EncryptionManager implements Initializable
{
    @FXML protected NavigationAnchor navigationAnchor;
    @FXML private static Stage stage;

    private IUserService userService = new UserService();

    void setStage(Node currentView)
    {
        stage = (Stage)currentView.getScene().getWindow();
        stage.setOnCloseRequest(event ->
        {
            FelixSession.getInstance().disconnect();
            System.out.println("System closed.");
            System.exit(0);
        });
    }

    void openNewView(final View view)
    {
        Platform.runLater(() ->
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + view.getPage() + ".fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/custom/navstyle.css").toExternalForm());
                stage.setTitle("Felix - " + view.getPage().substring(view.getPage().indexOf("/") + 1).substring(0, 1).toUpperCase() + view.getPage().substring(view.getPage().indexOf("/") + 2));
                stage.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
            e.printStackTrace();
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
}