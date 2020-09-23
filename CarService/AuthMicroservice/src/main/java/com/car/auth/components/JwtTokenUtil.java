package com.car.auth.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

  private String secret = "vcareucareallthetimeeverytimesdnfskbfabfkjdfnkasjdnfkabfkebfakjsdfkjsfnakjebfakefa";

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  //retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  //retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  //for retrieveing any information from token we will need the secret key
  public Claims getAllClaimsFromToken(String token) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  //check if the token has expired
  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  //generate token for user
  public String generateToken(UserDetails userDetails) {
    Map<String, String> claims = new HashMap<>();
    claims.put("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority());
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, String> claims, String subject) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(key).compact();
  }

  //validate token
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
