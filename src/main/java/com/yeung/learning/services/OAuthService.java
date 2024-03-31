package com.yeung.learning.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yeung.learning.dtos.TokenRequest;

@Service
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public String getGoogleToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        TokenRequest request = new TokenRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setCode(code);
        request.setGrantType("authorization_code");
        request.setRedirectUri("http://localhost:8080/api/oauth/google");
        String result = restTemplate.postForObject(
                "https://accounts.google.com/o/oauth2/token",
                request,
                String.class);
        System.out.println(result);
        return result;
    }
}
