package felix.api.configuration;

import felix.api.controller.WebSocket;
import felix.api.models.JwtToken;
import felix.api.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenGenerator
{
    private static final int TTL = 1_800_000;
    private static final String NAME = "username";
    private static final String DISPLAY_NAME = "displayName";
    private static final String USER_ID = "id";
    private static final String TWO_FA = "twofa";

    public JwtToken createJWT(User account) throws NoSuchAlgorithmException
    {
        SecretKey key = KeyGenerator.getInstance(SignatureAlgorithm.HS512.getJcaName()).generateKey();
        byte[] KEY = Arrays.toString(key.getEncoded()).getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(KEY, SignatureAlgorithm.HS256.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setSubject("Felix chat app")
                .claim(USER_ID, account.getId())
                .claim(DISPLAY_NAME, account.getDisplayName())
                .claim(NAME, account.getName())
                .claim(TWO_FA, account.isTwoFAEnabled())
                .setIssuer("Felix")
                .signWith(signingKey)
                .setExpiration(new Date(System.currentTimeMillis() + TTL));
        return new JwtToken(builder.compact(), KEY);
    }

    public User decodeJWT(String jwt)
    {
        try
        {
            User user = new User();
            Claims claims = Jwts.parser().setSigningKey(WebSocket.getKeyFromSession(jwt)).parseClaimsJws(jwt).getBody();
            user.setName(claims.get(NAME).toString());
            user.setDisplayName(claims.get(DISPLAY_NAME).toString());
            user.setId(UUID.fromString(claims.get(USER_ID).toString()));
            user.setTwoFAEnabled(Boolean.getBoolean(claims.get(TWO_FA).toString()));
            return user;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}