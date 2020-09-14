package felix.client.controller;

import felix.client.models.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import felix.client.main.Main;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

abstract class MainController implements Initializable
{
    @FXML
    private static Stage stage;
    protected Session session;

    void setStage(Node currentView)
    {
        stage = (Stage)currentView.getScene().getWindow();
        stage.setOnCloseRequest(event ->
        {
            try
            {
                this.session.close();
            }
            catch (Exception ignored) {}
            System.exit(0);
            System.out.println("System closed.");
        });
    }

    void connectToServer(String server)
    {
        WebSocketContainer container;
        try
        {
            container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(new EventClientSocket(), URI.create(server));
        }
        catch (DeploymentException | IOException e)
        {
            e.printStackTrace();
        }
    }

    void openNewView(final View view)
    {
        if (!view.equals(View.PAGE_NOT_FOUND))
        {
            //todo
        }
        Platform.runLater(() ->
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/" + view.getPage() + ".fxml"));
                Scene scene = new Scene(loader.load(), stage.getScene().getWidth(), stage.getScene().getHeight());
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/view/navBar.css").toExternalForm());
                stage.setTitle("Sysma - " + view.getPage().substring(view.getPage().indexOf("/") + 1).substring(0, 1).toUpperCase() + view.getPage().substring(view.getPage().indexOf("/") + 2));
                stage.show();
            }
            catch (Exception e)
            {
                if (view.equals(View.PAGE_NOT_FOUND))
                {
                    return;
                }
                openNewView(View.PAGE_NOT_FOUND);
            }
        });
    }
}