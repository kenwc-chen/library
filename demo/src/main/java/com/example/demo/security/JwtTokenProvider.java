package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 直接硬編碼密鑰和過期時間
    private final SecretKey jwtSecretKey = Keys.hmacShaKeyFor("2866af9b4db5c1edf8e5e83566aa83e108ae0ff2484e5a3141d9b4b99c8f3b79".getBytes());
    private final long jwtExpirationInMs = 86400000;  // 1天（單位：毫秒）

    // 生成 JWT token，將 phoneNumber 作為 subject
    public String createToken(String phoneNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)  // 使用 SecretKey 進行簽名
                .compact();
    }

    // 從 token 中解析出手機號碼 (subject)
    public String getPhoneNumberFromToken(String token) {
        Claims claims = Jwts.parserBuilder()  
                .setSigningKey(jwtSecretKey)  // 使用 SecretKey
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 驗證 JWT token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)  // 使用 SecretKey 驗證簽名
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;  // 如果 token 無效或過期，則返回 false
        }
    }
}
