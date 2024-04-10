package de.fschili.dlx.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {

    private final static Logger log = LoggerFactory.getLogger(JWTUtil.class);

    private static final String SECRET = "super-secret-key-which-is-very-long";
    private static final long EXPIRATION_TIME = 1000 * 60 * 10; // 10 minutes

    public static String generateJwt(String subject) {
        Date now = new Date();
        String jwt = Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .notBefore(now)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .issuer("CHILI GmbH")
                .signWith(getSecretKey())
                .compact();

        log.trace("Generated JWT for subject '" + subject + "': " + jwt + "(exp: " + EXPIRATION_TIME + ")");

        return jwt;
    }

    public static String extractSubject(String jwt) {
        Claims claims = getValidatedClaims(jwt);
        return claims != null ? claims.getSubject() : null;
    }

    public static String extractIssuer(String jwt) {
        Claims claims = getValidatedClaims(jwt);
        return claims != null ? claims.getIssuer() : null;
    }

    public static Set<String> extractAudience(String jwt) {
        Claims claims = getValidatedClaims(jwt);
        return claims != null ? claims.getAudience() : null;
    }

    public static Date extractStartDate(String jwt) throws Exception {
        Claims claims = getValidatedClaims(jwt);
        return claims != null ? claims.getNotBefore() : null;
    }

    public static Date extractExpiration(String jwt) throws Exception {
        Claims claims = getValidatedClaims(jwt);
        return claims != null ? claims.getExpiration() : null;
    }

    public static Claims getValidatedClaims(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        }
        catch (Exception e) {
            log.error("Token is not valide. (" + e + ")");
            return null;
        }
    }

    private static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static void printJwt(String jwt) {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String[] chunks = jwt.split("\\.");

        String header = new String(decoder.decode(chunks[0]));
        System.out.println("header:    " + header);
        if (chunks.length > 1) {
            String payload = new String(decoder.decode(chunks[1]));
            System.out.println("payload:   " + payload);
        }
        if (chunks.length > 2) {
            String signature = new String(decoder.decode(chunks[2]));
            System.out.println("signature: " + (signature != null ? "available" : "none") + " (valide: " + (getValidatedClaims(jwt) != null) + ")");
        }
    }

    public static void main(String[] args) {
        String token = generateJwt("AA1-B24-X7H");
        System.out.println("token: " + token);

        System.out.println();
        System.out.println("validating token...");
        Claims claims = getValidatedClaims(token + "d");
        System.out.println("claims: " + claims + " (valid: " + (claims != null) + ")");

        System.out.println();
        printJwt(token);
    }
}
