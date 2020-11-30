package felix.api.configuration;

import felix.api.models.Licence;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class LicenceManager
{
    private static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCgWat240WzGqwSCmqoTa5Y4btLCEpcjeZjQ1b/Myih2ikTfCBuBFeadOeGqBmpmV5FLHKvHN0bNtUGMrhRbdkF9wOdLJ0HPBTgMdmDY1l6fZ9vA0yBmTAAh2JIWfIKWisOjF524EniIl7p3jj8mOZlxuI0xcA18eoqanPCMpuOiIQKHtF7HQ4ipxzRWhJpCkBDfCxALTIqlDcKUKjBEtL03qiMpmgJEZCg8APjOzMp4Y4pCbS81Q/6PwPfNUL38rPFmTH0Yw8v7Y03Kuh6AVHfTbBY1tu1HDhw5fkpEq75VjzcVLY6Qnx3aoio/jTsfY1GXHjHZGE8fDRvFIiOmVtNAgMBAAECggEAIZppCmMaaiw+vxEbxOVykgHMJC86libOjyK/in+IFlMHpKOwkB2s1huFoXx81EPls41bA0ODOn7FKaVbZwUkI9HeGgQNbES1Az6vHI+nYroMvVDtqYMWwiWc/RiMVosK1uOnsDXUheU6CrydKKJKNi31naWLYEKwFe91Ds9YNggk2Aj2QmSjUXIbsVkGb48mw6lT21zH0g4+MarRI/tLAqb6TTQATCXIDDHl63rXVhES4M8L+08m1SCB8Vajzmxrk5z7dxIWnG7NZEGYSKRsNykMmL+u3ZNzdiOds326UkVuF8NEy1e3QpQBeOU4dVh07ZGT4q3Wljm9S06huRJ/QQKBgQDSotFzfdprpnAiytpYvv4M1torCC10zG9GCCQjgh6LgiBFK5w5RwJ5Myk0GH3YFMy7ZIrLdv1sOOjQBZnLpA/0Qg3ZXZ6d4HeYNaNVSvz0x6h+hFXB9afFYAToB0g8wd9HKOIWFeehUxeSRWxidqTazQaFpCOKY7Ec1+WIElM4mQKBgQDC4mdO8Leyz+sg4YqT45P3uW1pejIoO7JWvyBHFfA8AR2CIaiX9W3Q0kinpzw0G6wWPv+WDsvbS/4NpCMknxMjH+wkNbyLlTjb3BUUKsqQZaXx3KBGDM3wVWEWqoGa4414TpSmzb6a+lxbI6jC3eoaCWAQD996bd5zBKzzcWvk1QKBgDNhB9o2DrtHg/7YoF4ZaHrVRa5NdlkRLOtWPQp0SOMawKhetOK70Xyi4CAXAUtd93qWUczDn5lwYEmttBAaWtHn8tzcmz5DbKiyHL9HZWCH+y+xNAH13BCnFUtLy3EWkVIYRN3CRhfx3sQNLxztd9TvQfIDUbAGFUPF3fVNEwOxAoGAEtH8ehIjG2Ca62T8lgWfT4gXudz0JRCGy3vPqS3EMndFG0EW3/3yqfz0yUQMO1gEyW0yD5LEOxxen4CfaDT4iybVDjYmTDCP+iOJ4ZW0VDeMzAurarhn1vLX2nGC7KtXCSxFjMpHwv20l5SdApyds//9x+9QfKlJlz/3V9c3PTUCgYAh2h4Ua6EpuxKjhpS1dJc+5eRN0VfafrR5KCNjgWpi/MKzq/YMwsZFVcVKwCiAp6tGpUDHQ6LD46zsulJLfM/WMqU6y/hYlqyWMPdD9DlKDBjsxqwY/nxGNqX7t0btx07yRteD0ClN9mDl1rG/Gq7UXIAlMKqlbZ5Oq0FnaERF+g==";
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoFmrduNFsxqsEgpqqE2uWOG7SwhKXI3mY0NW/zMoodopE3wgbgRXmnTnhqgZqZleRSxyrxzdGzbVBjK4UW3ZBfcDnSydBzwU4DHZg2NZen2fbwNMgZkwAIdiSFnyClorDoxeduBJ4iJe6d44/JjmZcbiNMXANfHqKmpzwjKbjoiECh7Rex0OIqcc0VoSaQpAQ3wsQC0yKpQ3ClCowRLS9N6ojKZoCRGQoPAD4zszKeGOKQm0vNUP+j8D3zVC9/KzxZkx9GMPL+2NNyroegFR302wWNbbtRw4cOX5KRKu+VY83FS2OkJ8d2qIqP407H2NRlx4x2RhPHw0bxSIjplbTQIDAQAB";
    private static final String SIGN_INSTANCE = "SHA512withRSA";
    private static final String RSA = "RSA";

    public String sign(Licence licence) throws GeneralSecurityException
    {
        KeyFactory factory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY)));
        Signature signature = Signature.getInstance(SIGN_INSTANCE);
        signature.initSign(privateKey);
        signature.update(licence.toSignString().getBytes());
        byte[] digitalSignature = signature.sign();
        return new String(Base64.getEncoder().encode(digitalSignature));
    }

    public boolean verify(Licence licence) throws GeneralSecurityException
    {
        KeyFactory factory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY)));
        Signature signature = Signature.getInstance(SIGN_INSTANCE);
        signature.initVerify(publicKey);
        signature.update(licence.toSignString().getBytes());
        return signature.verify(Base64.getDecoder().decode(licence.getSign()));
    }

    public static String getAlgorithm()
    {
        return SIGN_INSTANCE;
    }
}