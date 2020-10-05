package felix.client.controller;

import felix.client.exceptions.AlreadyLoggedInException;
import felix.client.exceptions.NotAuthorizedException;
import felix.client.main.FelixSession;
import felix.client.models.User;
import felix.client.models.View;
import felix.client.service.user.IUserService;
import felix.client.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AuthenticationController extends MainController
{
    private static final String RED_BORDER = "-fx-border-color: red;";

    @FXML private TextField textFieldUsername;
    @FXML private PasswordField textFieldPassword;
    @FXML private TextField textFieldDisplayName;
    @FXML private PasswordField textFieldRetypePassword;
    @FXML private GridPane mainGrid;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label loginFailedLabel;
    private boolean isLogin = false;

    private IUserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() ->
        {
            super.setStage(this.mainGrid);
            this.textFieldUsername.requestFocus();
        });
        this.isLogin = loginButton != null;
        this.initializeNodes();
    }

    private void initializeNodes()
    {
        this.textFieldUsername.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                if (this.isLogin) this.textFieldPassword.requestFocus();
                else this.textFieldDisplayName.requestFocus();
            }
        });
        this.textFieldPassword.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                if (this.isLogin) this.login();
                else this.textFieldRetypePassword.requestFocus();
            }
        });
        if (this.isLogin) this.loginButton.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER) || key.getCode().equals(KeyCode.SPACE)) this.login();
        });
        if (!this.isLogin) this.registerButton.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER) || key.getCode().equals(KeyCode.SPACE)) this.register();
        });
        if (!this.isLogin) this.textFieldDisplayName.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER)) this.textFieldPassword.requestFocus();
        });
        if (!this.isLogin) this.textFieldRetypePassword.addEventHandler(KeyEvent.KEY_PRESSED, key ->
        {
            if (key.getCode().equals(KeyCode.ENTER)) this.register();
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
            //todo 2FA when implemented.
            User user = new User(this.textFieldUsername.getText(), this.textFieldPassword.getText());
            Map<String, String> encryptedUserInfo = FelixSession.getInstance().getEncryptedUserInfo(user);
            user.setEncryptedUUID(encryptedUserInfo.get("uuid"));
            user.setName(encryptedUserInfo.get("name"));
            user.setPassword(encryptedUserInfo.get("password"));
            FelixSession.getInstance().setToken(this.userService.login(user));
        }
        catch (NotAuthorizedException e)
        {
            this.loginFailed("Login failed.");
            return;
        }
        catch (AlreadyLoggedInException e)
        {
            this.loginFailed("This account is already logged in.");
            return;
        }
        catch (Exception e)
        {
            this.loginFailed("Encryption failure.");
            return;
        }
        super.openNewView(View.HOME);
    }

    private void loginFailed(String reason)
    {
        FelixSession.getInstance().setToken(null);
        this.textFieldUsername.clear();
        this.textFieldPassword.clear();
        this.textFieldUsername.requestFocus();
        this.loginFailedLabel.setText(reason);
        this.loginFailedLabel.setVisible(true);
    }

    public void registerLink_Click()
    {
        super.openNewView(View.REGISTER);
    }

    private void register()
    {
        this.textFieldUsername.setStyle(null);
        this.textFieldPassword.setStyle(null);
        this.textFieldRetypePassword.setStyle(null);
        this.loginFailedLabel.setVisible(false);
        if (this.textFieldUsername.getText().isEmpty() || this.textFieldPassword.getText().isEmpty() || this.textFieldRetypePassword.getText().isEmpty())
        {
            this.textFieldUsername.setStyle(this.textFieldUsername.getText().isEmpty() ? RED_BORDER : null);
            this.textFieldDisplayName.setStyle(this.textFieldDisplayName.getText().isEmpty() ? RED_BORDER : null);
            this.textFieldPassword.setStyle(this.textFieldPassword.getText().isEmpty() ? RED_BORDER : null);
            this.textFieldRetypePassword.setStyle(this.textFieldRetypePassword.getText().isEmpty() ? RED_BORDER : null);
            return;
        }
        if (!this.textFieldPassword.getText().equals(this.textFieldRetypePassword.getText()))
        {
            this.textFieldPassword.setStyle(RED_BORDER);
            this.textFieldRetypePassword.setStyle(RED_BORDER);
            this.loginFailedLabel.setText("Passwords doesn't match");
            this.loginFailedLabel.setVisible(true);
            return;
        }
        try
        {
            User user = new User(this.textFieldUsername.getText(), this.textFieldDisplayName.getText(), this.textFieldPassword.getText());
            Map<String, String> encryptedUserInfo = FelixSession.getInstance().getEncryptedUserInfo(user);
            user.setName(encryptedUserInfo.get("name"));
            user.setDisplayName(encryptedUserInfo.get("disp"));
            user.setEncryptedUUID(encryptedUserInfo.get("uuid"));
            user.setPassword(encryptedUserInfo.get("password"));
            FelixSession.getInstance().setToken(this.userService.register(user));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        super.openNewView(View.HOME);
    }

    public void registerButton_Click()
    {
        this.register();
    }
}