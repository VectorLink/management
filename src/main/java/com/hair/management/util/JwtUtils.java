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

@Slf4j
@Data
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtUtils {
    private String secret;
    private long expire;


    public String generateToken(Long hairMasterId) {
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

    public Claims getClaimByToken(String token) {
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
    public Boolean isTokenExpired(LocalDateTime localDateTime){
            return localDateTime.isBefore(LocalDateTime.now());
    }
}