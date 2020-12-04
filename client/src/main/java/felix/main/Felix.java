package felix.main;

import felix.fxml.messageBox.CustomOkMessage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Felix extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(super.getClass().getResource("/view/login.fxml"));
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/logo.png")));
        Parent parent = loader.load();
        primaryStage.setTitle("Felix");
        primaryStage.setMaxWidth(806);
        primaryStage.setMaxHeight(580);
        primaryStage.setResizable(false);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("/custom/navstyle.css").toExternalForm());
        primaryStage.setScene(scene);
        synchronized (FelixSession.class)
        {
            if (!FelixSession.connectToServer())
            {
                new CustomOkMessage(primaryStage, "Error while connecting, application shutting down.").show();
                System.exit(0);
                return;
            }
        }
        if (!FelixSession.getInstance().isConnected())
        {
            new CustomOkMessage(primaryStage, "Client is not connected, application shutting down.").show();
            System.exit(0);
            return;
        }
        if (!new LicenceChecker().checkLicence(true, scene))
        {
            new CustomOkMessage(primaryStage, LicenceChecker.getMessage()).show();
            System.exit(0);
            return;
        }
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}