package com.hair.management.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hair.management.bean.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Data
public class JwtUtils {
    private static final String secret="secret_hair";
    private static final long expire=604800;


    public static String generateToken(Long hairMasterId) {
        //过期时间
        LocalDateTime expireDate = LocalDateTime.now().plusSeconds(expire);
        return JWT.create().withClaim(Constants.CLAIM_TOKEN,hairMasterId)
                .withExpiresAt(Date.from(expireDate.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secret));
    }

    public static Long getHairMasterIdByToken(String token) {
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaim(Constants.CLAIM_TOKEN).asLong();
        } catch (Exception e) {
            log.error("invalid token {}", token, e);
            return null;
        }
    }

    public static Boolean verify(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(Constants.CLAIM_TOKEN, getHairMasterIdByToken(token)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.info("错误：",e);
            return false;
        }
    }
}
