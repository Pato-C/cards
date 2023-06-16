package com.logicea.cards.component;

import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    @Autowired
    private JwtKeyProvider jwtKeyProvider;
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    public String generateToken(UserEntity user) {
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
            throw new RuntimeException("Error generating token!!");
        }
    }
    public UserEntity getUserFromToken(String token) {
        logger.info("Going to introspect token");
        try {
            SecretKey signingKey = jwtKeyProvider.getSigningKey();
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
            String userEmail = claims.getSubject();
            Optional<UserEntity> userOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));
            if (userOptional.isPresent()) {
                return userOptional.get();
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            logger.error("Error introspecting token " + e);
            throw new RuntimeException("Invalid token!!");
        }
    }

}
