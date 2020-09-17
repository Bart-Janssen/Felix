package felix.client.controller;

import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
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
import java.nio.ByteBuffer;

abstract class MainController implements Initializable
{
    @FXML
    private static Stage stage;
    private Session session;
    private static JwtToken token;
    private static HeartBeatThread heartBeatThread;

    void setStage(Node currentView)
    {
        stage = (Stage)currentView.getScene().getWindow();
        stage.setOnCloseRequest(event ->
        {
            try
            {
                this.session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Client logged off"));
            }
            catch (Exception ignored) {}
            System.out.println("System closed.");
            System.exit(0);
        });
    }

    private String getToken()
    {
        return MainController.token == null ? null : MainController.token.getToken();
    }

    void connectToServer() throws NotAuthorizedException
    {
        if (this.getToken() == null) throw new NotAuthorizedException();
        try
        {
            this.session = ContainerProvider.getWebSocketContainer().connectToServer(new EventClientSocket(), URI.create("ws://localhost:6666/server/" + this.getToken()));
            heartBeatThread = new HeartBeatThread(session);
            heartBeatThread.start();
        }
        catch (DeploymentException | IOException e)
        {
            e.printStackTrace();
        }
    }

    void setJwtToken(JwtToken token)
    {
        MainController.token = token;
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
                //scene.getStylesheets().add(getClass().getResource("/view/navBar.css").toExternalForm());
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

    void stopHeartbeat()
    {
        heartBeatThread.stop();
    }

    void sendMessage(String text)
    {
        session.getAsyncRemote().sendText(text);
    }
}