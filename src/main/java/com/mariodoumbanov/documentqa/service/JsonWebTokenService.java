package com.mariodoumbanov.documentqa.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static org.springframework.security.config.Elements.JWT;

@Service
public class JsonWebTokenService {
    private static final String JWT_SECRET = "JWTULRASECRET1234WWASDJASPJDPA@ASPDJP";
    private static final int day = 1000 * 60 * 60 * 24;

    public String generate(Integer id, String email) throws
    IllegalArgumentException, JWTCreationException {
        return com.auth0.jwt.JWT.create()
                .withSubject("User Details")
                .withClaim("id", id)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + day))
                .withIssuer("Documentary")
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    public String validate(String token) throws JWTVerificationException {
        var verification = com.auth0.jwt.JWT.require(Algorithm.HMAC256(JWT_SECRET));
        var verifier = verification.withSubject("User Details").withIssuer("Documentary")
                .build();

        return verifier.verify(token).getClaim("email").asString();
    }
}
