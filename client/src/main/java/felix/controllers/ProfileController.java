package felix.controllers;

import felix.main.FelixSession;
import felix.models.User;
import felix.service.system.JwtDecoder;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends MainController
{
    @FXML private ImageView qrCode;
    @FXML private Button buttonTwoFa;
    @FXML private Label labelName;
    @FXML private Label labelDisplayName;
    @FXML private GridPane main;
    private IUserService userService = new UserService();

    private User user = null;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> super.initController(this.main));
        Platform.runLater(this::setUser);
        this.initializeEvents();
    }

    private void setUser()
    {
        this.user = super.getUser();
        this.labelName.setText("Name: " + this.user.getName());
        this.labelDisplayName.setText("Displayname: " + this.user.getDisplayName());
        this.buttonTwoFa.setText(this.user.hasTwoFAEnabled() ? "Disable 2FA" : "Enable 2FA");
    }

    private void initializeEvents()
    {
        this.buttonTwoFa.setOnMouseClicked(event ->
        {
            if (this.user.hasTwoFAEnabled())
            {
                disable2Fa();
                qrCode.setImage(null);
                this.setUser();
                return;
            }
            try
            {
                qrCode.setImage(new Image(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(enable2Fa()))));
                this.setUser();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    private String enable2Fa()
    {
        try
        {
            return this.userService.enable2Fa();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    private void disable2Fa()
    {
        this.userService.disable2Fa();
    }
}