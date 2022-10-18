package com.authexample.authorization.services.jwt;

import com.authexample.authorization.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTTokenProvider {

  private final UserDetailsService userDetailsServiceImpl;

  private String secretKey;
  private long validityInSeconds;
  private String authorizationHeader;

  @PostConstruct
  public void init(){
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username, Role role) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("role", role.name());

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInSeconds * 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    }
    catch (JwtException | IllegalArgumentException e){
      throw new JWTAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
    }
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities()
    );
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) throws NullPointerException {
    return request.getHeader(authorizationHeader);
  }
}
