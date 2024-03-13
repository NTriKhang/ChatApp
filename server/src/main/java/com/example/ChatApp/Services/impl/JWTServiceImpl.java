package com.example.ChatApp.Services.impl;

import com.example.ChatApp.Services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bson.codecs.Decoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    public String generateToken(UserDetails userDetails)
    {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSiginKey(), SignatureAlgorithm.ES256)
                .compact();
    }
    public String extractUserName(String token){
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
    public boolean isTokenValid(String token,UserDetails userDetails)
    {
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
