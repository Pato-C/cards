package com.logicea.cards.component;

import com.logicea.cards.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    @Autowired
    private JwtKeyProvider jwtKeyProvider;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    public String generateToken(User user) {
        try {
            SecretKey signingKey = jwtKeyProvider.getSigningKey();
            Claims claims = Jwts.claims().setSubject(user.getEmail());
            claims.put("role", user.getRole());
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime);
             return Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("role", user.getRole())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(signingKey, SignatureAlgorithm.HS256)
                    .compact();
        }
        catch (Exception e)
        {
         logger.error("Error generating token "+e);
        }
        return null;
    }

}
