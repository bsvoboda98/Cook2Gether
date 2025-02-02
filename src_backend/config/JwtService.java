package com.bee.cookwithfriends.config;

import com.bee.cookwithfriends.entity.User;
import com.bee.cookwithfriends.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration}")
    private long jwtAccessExpiration;

    @Value("${security.jwt.refresh-token-expiration}")
    private long jwtRefreshExpiration;

    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository){
        this.userRepository = userRepository;
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Generation Access Token
    public String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtAccessExpiration);
    }

    public long getAccessExpirationTime() {
        return jwtAccessExpiration;
    }

    //Generating Refresh Token
    public String generateRefreshToken(User user) {
        return generateRefreshToken(new HashMap<>(), user);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtRefreshExpiration);
    }

    public long getRefreshExpirationTime() {
        return jwtRefreshExpiration;
    }

    //refreshing the accessToken
    public String refreshAccessToken(String refreshToken){
        String username = extractUsername(refreshToken);
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found", null);
        }

        return generateAccessToken(user);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
