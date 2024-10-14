package com.maids.cc.Library.Management.System.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "e9ffd9cf78065572be04371da941845f5b595aec6c987e6d38afaa00e07d8ebc";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);//subject should be the email or the username of the user
    }

    public <T> T extractClaim(String jwtToken, Function<Claims,T> claimsResolver){//Function from java.util take claims and return T
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    //method help us generate a token

    public String generateToken(
            Map<String , Object> extraClaims,//will contain the extra claims that we want to add //if i want to pass authority for example
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))//will help us to calculate the expiration date or to check if the token is still valid
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();//generate and compact a token
    }


    //method to validate a token
    public boolean isTokenValid(String token, UserDetails userDetails){//we want to validate if that token belongs to that userDetails
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return (extractClaim(token,Claims::getExpiration));
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
