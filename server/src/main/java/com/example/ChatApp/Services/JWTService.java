package com.example.ChatApp.Services;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService{
    public String generateToken(String userId)
    {
        return Jwts.builder().setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSiginKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String extractUserId(String token){
        return extractClaim(token,Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers)
    {
        final Claims claims = extractAllClaim(token);
        return claimsResolvers.apply(claims);
    }
    private Key getSiginKey()
    {
        byte[] key= Decoders.BASE64.decode("412F4428472B4B625065536866D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(key);

    }
    private Claims extractAllClaim(String token)
    {
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }
    public boolean isTokenValid(String token,Users userDetails)
    {
        final String username=extractUserId(token);
        return (username.equals(userDetails.Tag)&&!isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
