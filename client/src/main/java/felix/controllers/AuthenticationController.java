package felix.controllers;

import felix.exceptions.AlreadyLoggedInException;
import felix.exceptions.NotAuthorizedException;
import felix.fxml.messageBox.CustomOkMessage;
import felix.main.FelixSession;
import felix.models.User;
import felix.models.View;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML private TextField textFieldUsername;
    @FXML private Button backToLoginButton;
    @FXML private PasswordField textFieldPassword;
    @FXML private TextField textFieldDisplayName;
    @FXML private PasswordField textFieldRetypePassword;
    @FXML private GridPane mainGrid;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    private boolean isLogin = false;

    private IUserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() ->
        {
            super.initController(this.mainGrid);
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
    }

    public void loginButton_Click()
    {
        this.login();
    }

    private void login()
    {
        this.textFieldUsername.setStyle(DEFAULT);
        this.textFieldPassword.setStyle(DEFAULT);
        if (this.textFieldUsername.getText().isEmpty() || this.textFieldPassword.getText().isEmpty())
        {
            this.textFieldUsername.setStyle(this.textFieldUsername.getText().isEmpty() ? RED_BORDER : DEFAULT);
            this.textFieldPassword.setStyle(this.textFieldPassword.getText().isEmpty() ? RED_BORDER : DEFAULT);
            return;
        }
        try
        {
            User user = new User(this.textFieldUsername.getText(), this.textFieldPassword.getText());
            Map<String, String> encryptedUserInfo = FelixSession.getInstance().getEncryptedUserInfo(user);
            user.setSessionId(encryptedUserInfo.get("sessionId"));
            user.setName(encryptedUserInfo.get("name"));
            user.setPassword(encryptedUserInfo.get("password"));
            this.userService.login(user);
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
            this.loginFailed("General login failure.");
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
        new CustomOkMessage(stage, reason).show();
    }

    private void registerFailed(String reason)
    {
        FelixSession.getInstance().setToken(null);
        this.textFieldUsername.clear();
        this.textFieldDisplayName.clear();
        this.textFieldPassword.clear();
        this.textFieldRetypePassword.clear();
        this.textFieldUsername.requestFocus();
        new CustomOkMessage(stage, reason).show();
    }

    public void registerLink_Click()
    {
        super.openNewView(View.REGISTER);
    }

    private void register()
    {
        this.textFieldUsername.setStyle(DEFAULT);
        this.textFieldPassword.setStyle(DEFAULT);
        this.textFieldRetypePassword.setStyle(DEFAULT);
        if (this.textFieldUsername.getText().isEmpty() || this.textFieldPassword.getText().isEmpty() || this.textFieldRetypePassword.getText().isEmpty())
        {
            this.textFieldUsername.setStyle(this.textFieldUsername.getText().isEmpty() ? RED_BORDER : DEFAULT);
            this.textFieldDisplayName.setStyle(this.textFieldDisplayName.getText().isEmpty() ? RED_BORDER : DEFAULT);
            this.textFieldPassword.setStyle(this.textFieldPassword.getText().isEmpty() ? RED_BORDER : DEFAULT);
            this.textFieldRetypePassword.setStyle(this.textFieldRetypePassword.getText().isEmpty() ? RED_BORDER : DEFAULT);
            return;
        }
        if (!this.textFieldPassword.getText().equals(this.textFieldRetypePassword.getText()))
        {
            this.textFieldPassword.setStyle(RED_BORDER);
            this.textFieldRetypePassword.setStyle(RED_BORDER);
            new CustomOkMessage(stage, "Passwords doesn't match.").show();
            return;
        }
        if (this.textFieldPassword.getText().length() < 8)
        {
            this.textFieldPassword.setStyle(RED_BORDER);
            this.textFieldRetypePassword.setStyle(RED_BORDER);
            new CustomOkMessage(stage, "Password need to be at least 8 characters.").show();
            return;
        }
        try
        {
            User user = new User(this.textFieldUsername.getText(), this.textFieldDisplayName.getText(), this.textFieldPassword.getText());
            Map<String, String> encryptedUserInfo = FelixSession.getInstance().getEncryptedUserInfo(user);
            user.setName(encryptedUserInfo.get("name"));
            user.setDisplayName(encryptedUserInfo.get("disp"));
            user.setSessionId(encryptedUserInfo.get("sessionId"));
            user.setPassword(encryptedUserInfo.get("password"));
            this.userService.register(user);
        }
        catch (AlreadyLoggedInException e)
        {
            this.registerFailed("This name is not available now.");
            return;
        }
        catch (Exception e)
        {
            this.registerFailed("Register failed.");
            return;
        }
        super.openNewView(View.HOME);
    }

    public void registerButton_Click()
    {
       this.register();
    }

    public void backToLoginButton_click()
    {
        super.openNewView(View.LOGIN);
    }
}