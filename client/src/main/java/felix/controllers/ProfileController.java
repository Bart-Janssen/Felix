package felix.controllers;

import felix.fxml.messageBox.CustomOkMessage;
import felix.main.FelixSession;
import felix.models.User;
import felix.service.system.JwtDecoder;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sun.misc.BASE64Decoder;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.IntBuffer;
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
                Image originalQr = new Image(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(enable2Fa())));
                WritableImage coloredQr = new WritableImage(200, 200);
                PixelWriter coloredQrWriter = coloredQr.getPixelWriter();
                for (int x = 0; x < 200; x++)
                {
                    for (int y = 0; y < 200; y++)
                    {
                        if (((originalQr.getPixelReader().getArgb(x, y) & 0xFF) == 0x00)
                             && ((originalQr.getPixelReader().getArgb(x, y) >> 8 & 0xFF) == 0x00)
                             && ((originalQr.getPixelReader().getArgb(x, y) >> 16 & 0xFF) == 0x00)) coloredQrWriter.setArgb(x, y, 0xFF909090);
                    }
                }
                qrCode.setImage(new ImageView(coloredQr).getImage());
                this.setUser();
            }
            catch (Exception e)
            {
                new CustomOkMessage(stage, "Error while setting QR code, disable 2FA and try again.").show();
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
            new CustomOkMessage(stage, "Error while enabling 2FA.").show();
            return "";
        }
    }

    private void disable2Fa()
    {
        try
        {
            this.userService.disable2Fa();
        }
        catch (Exception e)
        {
            new CustomOkMessage(stage, "Error while disabling 2FA.").show();
        }
    }
}