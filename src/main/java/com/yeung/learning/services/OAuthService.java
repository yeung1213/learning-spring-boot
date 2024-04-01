package com.yeung.learning.services;

import java.text.MessageFormat;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.yeung.learning.dtos.GoogleTokenResponse;
import com.yeung.learning.dtos.TokenRequest;

import jakarta.servlet.http.HttpSession;

@Service
public class OAuthService {

    @Autowired
    HttpSession httpSession;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.okta.client-id}")
    private String oktaClientId;

    @Value("${spring.security.oauth2.client.registration.okta.client-secret}")
    private String oktaClientSecret;

    public void getGoogleToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        TokenRequest request = new TokenRequest();
        request.setClientId(clientId);
        request.setClientSecret(clientSecret);
        request.setCode(code);
        request.setGrantType("authorization_code");
        request.setRedirectUri("http://localhost:8080/api/oauth/google/callback");
        GoogleTokenResponse res = restTemplate.postForObject(
                "https://accounts.google.com/o/oauth2/token",
                request,
                GoogleTokenResponse.class);
        System.out.println(res);
        if (res != null) {
            httpSession.setAttribute("token", res.geIdToken());
        }
    }

    public void getOktaToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (oktaClientId != null && oktaClientSecret != null) {
            headers.setBasicAuth(oktaClientId, oktaClientSecret);
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", "http://localhost:8080/api/oauth/okta/callback");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        @SuppressWarnings("null")
        ResponseEntity<GoogleTokenResponse> res = restTemplate.exchange(
                "https://dev-25918791.okta.com/oauth2/default/v1/token",
                HttpMethod.POST, entity, GoogleTokenResponse.class);

        System.out.println(res.getBody());
        // if (res != null) {
        // httpSession.setAttribute("token", res.geIdToken());
        // }
    }
}
