package felix.client.controller;

import felix.client.exceptions.NotAuthorizedException;
import felix.client.models.JwtToken;
import felix.client.models.User;
import felix.client.models.View;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthenticationController extends MainController
{
    private static final String RED_BORDER = "-fx-border-color: red;";

    @FXML private TextField textFieldUsername;
    @FXML private TextField textFieldPassword;
    @FXML private GridPane mainGrid;
    @FXML private Button loginButton;
    @FXML private Label loginFailedLabel;

    private IUserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.setStage(this.mainGrid));
        this.textFieldUsername.requestFocus();
        this.initializeNodes();
    }

    private void initializeNodes()
    {
        this.textFieldUsername.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER)) this.textFieldPassword.requestFocus();
        });
        this.textFieldPassword.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER)) this.login();
        });
        this.loginButton.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER) || key.getCode().equals(KeyCode.SPACE)) this.login();
        });
        this.textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> this.loginFailedLabel.setVisible(false));
    }

    public void loginButton_Click()
    {
        this.login();
    }

    private void login()
    {
        this.textFieldUsername.setStyle(null);
        this.textFieldPassword.setStyle(null);
        this.loginFailedLabel.setVisible(false);
        if (this.textFieldUsername.getText().isEmpty() || this.textFieldPassword.getText().isEmpty())
        {
            this.textFieldUsername.setStyle(this.textFieldUsername.getText().isEmpty() ? RED_BORDER : null);
            this.textFieldPassword.setStyle(this.textFieldPassword.getText().isEmpty() ? RED_BORDER : null);
            return;
        }
        try
        {
            super.setJwtToken(this.userService.login(new User(this.textFieldUsername.getText(), this.textFieldPassword.getText())));
        }
        catch (NotAuthorizedException e)
        {
            e.printStackTrace();
            this.loginFailed();
            return;
        }
        super.openNewView(View.HOME);
    }

    private void loginFailed()
    {
        super.setJwtToken(null);
        this.textFieldUsername.clear();
        this.textFieldPassword.clear();
        this.textFieldUsername.requestFocus();
        this.loginFailedLabel.setVisible(true);
    }
}