package com.dmaqrss.mpu_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dmaqrss.mpu_api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("mpu-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException ex){
            throw new RuntimeException("erro ao gerar token", ex);
        }
    }

    public String validadeToken(String token){
        token = token.replace("Bearer ", "").trim();
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("mpu-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant generateResetExpirationDate(){
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateResetToken(String email){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(generateResetExpirationDate())
                .sign(algorithm);
    }

    public String validateResetToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try{
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

        }catch (Exception e){
            throw new RuntimeException("token invalido ou expirado");
        }
    }
}
