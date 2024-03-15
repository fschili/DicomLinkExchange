package de.fschili.dlx;

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
                .signWith(getSecretKey())
                .compact();

        log.trace("Generated jwt for subject '" + subject + "': " + jwt);

        return jwt;
    }

    public static String extractSubject(String jwt) throws Exception {
        return validateJwt(jwt).getSubject();
    }

    public static String extractIssuer(String jwt) throws Exception {
        return validateJwt(jwt).getIssuer();
    }

    public static Set<String> extractAudience(String jwt) throws Exception {
        return validateJwt(jwt).getAudience();
    }

    public static Date extractExpiration(String jwt) throws Exception {
        return validateJwt(jwt).getExpiration();
    }

    public static Claims validateJwt(String jwt) throws Exception {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        }
        catch (Exception e) {
            log.error("Token is not valide: " + e, log.isDebugEnabled() ? e : null);
            return null;
        }
    }

    private static SecretKey getSecretKey() {
        //Jwts.SIG.HS256.key().build();

        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static void printJwt(String jwt) {
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String[] chunks = jwt.split("\\.");
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        System.out.println("header: " + header);
        System.out.println("payload: " + payload);
    }

    public static void main(String[] args) {
        String token = generateJwt("AA1-B24-X7H");
        System.out.println(token);

        try {
            System.out.println(validateJwt(token));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        printJwt(token);
    }
}
