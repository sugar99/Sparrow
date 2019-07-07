package com.micerlab.sparrow.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Objects;

public class JwtUtil {

    private static final String SECRET = "SPARROW";

    private static final long EXPIRE_TIME = 60 * 1000 * 60 * 24;

    public static String createToken(String user_id) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withClaim("user_id", user_id)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUser_id(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return null;
        }
        String token = "";
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("auth_token")) {
                token = cookie.getValue();
            }
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            String user_id = jwt.getClaim("user_id").asString();
            return user_id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verify(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return false;
        }
        String token = "";
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("auth_token")) {
                token = cookie.getValue();
            }
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
