package felix.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Felix extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(super.getClass().getResource("/view/login.fxml"));
        Parent parent = loader.load();
        primaryStage.setTitle("Felix");
        Scene scene = new Scene(parent, 800, 550);
        scene.getStylesheets().add(getClass().getResource("/custom/navstyle.css").toExternalForm());
        primaryStage.setScene(scene);
        FelixSession.connectToServer(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}