package com.hair.management.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class JwtUtils {
    private static final String secret="secret_hair";
    private static final long expire=604800;


    public static String generateToken(Long hairMasterId) {
        //过期时间
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(expire);
        return Jwts.builder().setHeaderParam("typ", "JWT")
                .setSubject(hairMasterId.toString())
                .setIssuedAt(Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("invalid token {}", token, e);
            return null;
        }
    }

    /**
     * 是否过期
     * @param localDateTime
     * @return
     */
    public static Boolean isTokenExpired(LocalDateTime localDateTime){
            return localDateTime.isBefore(LocalDateTime.now());
    }
}
