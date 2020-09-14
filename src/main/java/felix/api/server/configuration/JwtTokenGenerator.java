package felix.api.server.configuration;

import felix.api.server.models.JwtToken;
import felix.api.server.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenGenerator
{
    private static final String SECRET_KEY = "asjkdhakjshfkjasdhgfkjashdbfiweutrm8wvt93vt3eV*AENVta8t4yugsetuuekiurtPsdTNA$*A&VPNaTN&AntPVTNa*N&VTAV&*TNAT";
    private static final int TTL = 1800000;
    private static final String NAME = "username";
    private static final String DISPLAY_NAME = "displayName";
    private static final String USER_ID = "id";
    private static final String TWO_FA = "twofa";

    public JwtToken createJWT(User account)
    {
        Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setSubject("TradeMarketV2Token")
                .claim(USER_ID, account.getId())
                .claim(DISPLAY_NAME, account.getDisplayName())
                .claim(NAME, account.getName())
                .claim(TWO_FA, account.isTwoFAEnabled())
                .setIssuer("TradeMarketV2")
                .signWith(signingKey)
                .setExpiration(new Date(System.currentTimeMillis() + TTL));
        return new JwtToken(builder.compact());
    }

    public User decodeJWT(String jwt)
    {
        try
        {
            User user = new User();
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(jwt.replace("Bearer", "").trim()).getBody();
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