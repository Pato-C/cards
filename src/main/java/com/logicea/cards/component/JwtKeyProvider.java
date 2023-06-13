package com.logicea.cards.component;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class JwtKeyProvider {


    private static final String SECRET_KEY = Arrays.toString("in5uCIgQF".getBytes(StandardCharsets.UTF_8));;

    public SecretKey getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

}
