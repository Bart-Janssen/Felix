package felix.models;

import java.util.List;
import java.util.UUID;

public class Licence
{
    private UUID token;
    private String encryptedToken;
    private String publisher;
    private String application;
    private String algorithm;
    private String sign;
    private List<String> macs;

    public String toSignString()
    {
        return "Publisher:" + publisher + System.lineSeparator() +
                "Application:" + application + System.lineSeparator() +
                "Token:" + token + System.lineSeparator() +
                "SignAlgorithm:" + algorithm + System.lineSeparator();
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<String> getMacs() {
        return macs;
    }

    public void setMacs(List<String> macs) {
        this.macs = macs;
    }

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public void setEncryptedToken(String encryptedToken) {
        this.encryptedToken = encryptedToken;
    }
}