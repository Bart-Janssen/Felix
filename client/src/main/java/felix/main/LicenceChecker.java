package felix.main;

import felix.exceptions.BadRequestException;
import felix.models.Licence;
import felix.service.user.IUserService;
import felix.service.user.UserService;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

class LicenceChecker
{
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoFmrduNFsxqsEgpqqE2uWOG7SwhKXI3mY0NW/zMoodopE3wgbgRXmnTnhqgZqZleRSxyrxzdGzbVBjK4UW3ZBfcDnSydBzwU4DHZg2NZen2fbwNMgZkwAIdiSFnyClorDoxeduBJ4iJe6d44/JjmZcbiNMXANfHqKmpzwjKbjoiECh7Rex0OIqcc0VoSaQpAQ3wsQC0yKpQ3ClCowRLS9N6ojKZoCRGQoPAD4zszKeGOKQm0vNUP+j8D3zVC9/KzxZkx9GMPL+2NNyroegFR302wWNbbtRw4cOX5KRKu+VY83FS2OkJ8d2qIqP407H2NRlx4x2RhPHw0bxSIjplbTQIDAQAB";
    private static final String RSA = "RSA";
    private static final String PUBLISHER = "Publisher:";
    private static final String APPLICATION = "Application:";
    private static final String TOKEN = "Token:";
    private static final String SIGN_ALGORITHM = "SignAlgorithm:";
    private static final String SIGN = "Sign:";
    private static final String LIC_FILE_LOCATION = System.getenv("LOCALAPPDATA") + "\\Felix";
    private static final String LIC_FILE = "\\licence.lic";

    private IUserService userService = new UserService();

    void checkLicence(boolean skipVirtual, Scene scene) throws Exception
    {
        List<String> macs = this.getMacs();
        if (macs.size() == 0) this.checkLicence(false, scene);
        if (macs.size() == 0 && !skipVirtual) return;
        File directory = new File(LIC_FILE_LOCATION);
        if (!directory.exists()) directory.mkdir();
        if (!new File(LIC_FILE_LOCATION + LIC_FILE).exists())
        {
            this.activate(scene);
            return;
        }
        Licence licence = this.getLicenceFromFile(new File(LIC_FILE_LOCATION + LIC_FILE));
        if (!this.verify(licence)) return;
        licence.setMacs(macs);
        this.userService.checkLicence(licence);
    }

    private void activate(Scene scene) throws Exception
    {
        List<String> macs = this.getMacs();
        File selectedLicence = new FileChooser().showOpenDialog(scene.getWindow());
        Licence licence = this.getLicenceFromFile(selectedLicence);
        licence.setMacs(macs);
        if (!this.verify(licence)) return;
        try
        {
            this.userService.activate(licence);
            File licenceFile = new File(LIC_FILE_LOCATION + LIC_FILE);
            Files.write(licenceFile.toPath(), Files.readAllBytes(selectedLicence.toPath()));
        }
        catch (BadRequestException e)
        {
            System.out.println("This licence is already activated.");
        }
    }

    private boolean verify(Licence licence) throws GeneralSecurityException
    {
        KeyFactory factory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY)));
        Signature signature = Signature.getInstance(licence.getAlgorithm());
        signature.initVerify(publicKey);
        signature.update(licence.toSignString().getBytes());
        if (!signature.verify(Base64.getDecoder().decode(licence.getSign())))
        {
            System.out.println("Licence is not valid.");
            return false;
        }
        System.out.println("Licence is valid.");
        return true;
    }

    private List<String> getMacs() throws IOException
    {
        List<String> macs = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (hardwareAddress != null)
            {
                if (networkInterface.getDisplayName().toLowerCase().contains("tunnel") ||
                        networkInterface.getDisplayName().toLowerCase().contains("virtual") ||
                        networkInterface.getDisplayName().toLowerCase().contains("vm") ||
                        networkInterface.getDisplayName().toLowerCase().contains("tap") ||
                        networkInterface.getDisplayName().toLowerCase().contains("vpn")) continue;
                String[] hexadecimalFormat = new String[hardwareAddress.length];
                for (int i = 0; i < hardwareAddress.length; i++)
                {
                    hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                }
                macs.add(String.join("-", hexadecimalFormat));
            }
        }
        return macs;
    }

    private Licence getLicenceFromFile(File file) throws IOException
    {
        Licence licence = new Licence();
        for (String licenceLine : Files.readAllLines(file.toPath()))
        {
            if (licenceLine.startsWith(PUBLISHER)) licence.setPublisher(licenceLine.replace(PUBLISHER, ""));
            if (licenceLine.startsWith(APPLICATION)) licence.setApplication(licenceLine.replace(APPLICATION, ""));
            if (licenceLine.startsWith(TOKEN)) licence.setToken(UUID.fromString(licenceLine.replace(TOKEN, "")));
            if (licenceLine.startsWith(SIGN_ALGORITHM)) licence.setAlgorithm(licenceLine.replace(SIGN_ALGORITHM, ""));
            if (licenceLine.startsWith(SIGN)) licence.setSign(licenceLine.replace(SIGN, ""));
        }
        return licence;
    }
}