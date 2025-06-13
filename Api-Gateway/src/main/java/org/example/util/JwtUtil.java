package org.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        System.out.println("✅ JwtUtil 초기화 완료");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e) {
            System.out.println("❌ Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("❌ Malformed token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("❌ Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Illegal argument: " + e.getMessage());
        }
        return false;
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);

        return claims.getSubject();
    }
    public List<SimpleGrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        // 예: roles: ["ROLE_EMPLOYEE", "ROLE_ADMIN"]
        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
