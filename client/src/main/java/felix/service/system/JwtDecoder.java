package felix.service.system;

import felix.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.UUID;

public class JwtDecoder
{
    private static final String NAME = "username";
    private static final String DISPLAY_NAME = "displayName";
    private static final String USER_ID = "id";
    private static final String TWO_FA = "twofa";

    public User decode(String jwt)
    {
        Claims claims = Jwts.parser().parseClaimsJwt(jwt.substring(0, jwt.lastIndexOf('.') + 1)).getBody();
        return new User(UUID.fromString(claims.get(USER_ID).toString()), claims.get(NAME).toString(), claims.get(DISPLAY_NAME).toString(), Boolean.parseBoolean(claims.get(TWO_FA).toString()));
    }
}