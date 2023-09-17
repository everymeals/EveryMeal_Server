package everymeal.server.global.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret.access-key}")
    private String accessSecretKey;

    @Value("${jwt.secret.refresh-key}")
    private String refreshSecretKey;

    @Value("${jwt.validity.access-seconds}")
    private Long accessTokenExpirationMs;

    @Value("${jwt.validity.refresh-seconds}")
    private Long refreshTokenExpirationMs;

    public String generateAccessToken(Long idx) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("CLAIM_KEY_IDX", idx);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(accessSecretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long idx, String accessToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("CLAIM_KEY_IDX", idx);
        claims.put("CLAIM_KEY_ACCESS_TOKEN", accessToken);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(refreshSecretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }
}
