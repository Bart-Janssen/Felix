package licenceserver.main;

import java.util.List;
import java.util.UUID;

public class Licence
{
    private UUID token;
    private boolean activated;
    private static final String publisher = "Bart Janssen";
    private static final String application = "Felix";
    private static final String algorithm = "SHA512withRSA";
    private String sign;

    private String encryptedToken;

    private List<String> macs;

    public String toSignString()
    {
        return "Publisher:" + publisher + (char)0b00001101 + (char)0b00001010 +
                "Application:" + application + (char)0b00001101 + (char)0b00001010 +
                "Token:" + token + (char)0b00001101 + (char)0b00001010 +
                "SignAlgorithm:" + algorithm + (char)0b00001101 + (char)0b00001010;
    }

    @Override
    public String toString()
    {
        return "Publisher:" + publisher + (char)0b00001101 + (char)0b00001010 +
                "Application:" + application + (char)0b00001101 + (char)0b00001010 +
                "Token:" + token + (char)0b00001101 + (char)0b00001010 +
                "SignAlgorithm:" + algorithm + (char)0b00001101 + (char)0b00001010 +
                "Sign:" + sign;
    }

    public byte[] toByteArray()
    {
        return this.toString().getBytes();
    }

    public void setActivated(boolean b)
    {
        this.activated = b;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }
}