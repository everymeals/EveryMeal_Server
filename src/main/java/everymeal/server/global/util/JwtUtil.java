package everymeal.server.global.util;

import static everymeal.server.global.exception.ExceptionList.TOKEN_EXPIRATION;
import static everymeal.server.global.exception.ExceptionList.TOKEN_NOT_VALID;

import everymeal.server.global.exception.ApplicationException;
import everymeal.server.global.util.authresolver.entity.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
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

    public AuthenticatedUser getAuthenticateUserFromAccessToken(String token) {
        Claims claims = getClaimsFromToken(tokenSubBearer(token), accessSecretKey);
        if (claims != null) {
            return AuthenticatedUser.builder()
                    .idx(Long.parseLong(claims.get("CLAIM_KEY_IDX").toString()))
                    .email(claims.get("CLAIM_KEY_EMAIL").toString())
                    .nickName(claims.get("CLAIM_KEY_NICKNAME").toString())
                    .build();
        }
        return null;
    }

    private Claims getClaimsFromToken(String token, String secretKey) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException exception) {
            log.error("Token Tampered");
            throw new ApplicationException(TOKEN_EXPIRATION);
        } catch (MalformedJwtException exception) {
            log.error("Token MalformedJwtException");
            throw new ApplicationException(TOKEN_EXPIRATION);
        } catch (Exception exception) {
            log.error("Undefined ERROR");
            throw new ApplicationException(TOKEN_NOT_VALID);
        }
    }

    private static String tokenSubBearer(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 문자열 제거
        }
        return token;
    }
}
