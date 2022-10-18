package com.authexample.authorization.controllers;

import com.authexample.authorization.models.Account;
import com.authexample.authorization.services.jwt.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTestWrapper {

  private final TestRestTemplate REST = new TestRestTemplate();

  @Value("${server.servlet.context-path}")
  private String basePath;
  @LocalServerPort
  private int port;

  @Autowired
  protected JWTTokenProvider jwtTokenProvider;

  protected <T, V> ResponseEntity<V> exchange(
      String path, T dto, HttpMethod httpMethod, Class<V> returnType
  ) {
    return REST.exchange(
        String.format("%s/%s", getApiPath(), path), httpMethod, new HttpEntity<>(dto), returnType
    );
  }

  protected <T, V> ResponseEntity<V> exchangeWithAuthentication(
      String path, T dto, Account account, HttpMethod httpMethod, Class<V> returnType
  ) {
    return REST.exchange(
        String.format("%s/%s", getApiPath(), path), httpMethod, generateWithJwtToken(dto, account), returnType
    );
  }

  private  <T> HttpEntity<T> generateWithJwtToken(T body, Account account){
    String token = jwtTokenProvider.createToken(account.getLogin(), account.getRoles());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", token);

    if(body == null){
      return new HttpEntity<>(headers);
    }
    return new HttpEntity<>(body, headers);
  }


  private String getApiPath() {
    return String.format("http://localhost:%s%s", port, basePath);
  }

}
