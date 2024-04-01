package com.yeung.learning.restcontrollers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yeung.learning.services.OAuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/oauth")
public class OAuthRestcontroller {
    @Autowired
    OAuthService oAuthService;

    @GetMapping("google/callback")
    public void googleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        System.out.println("~~~~~");
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        oAuthService.getGoogleToken(code);
        System.out.println(code);
        response.sendRedirect("http://localhost:3000/about");
        // return new MyResponse();
        return;
    }

    @GetMapping("okta/callback")
    public void oktaCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        System.out.println("~~~~~~~~~~ okta/callback");
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        System.out.println("code: " + code);
        oAuthService.getOktaToken(code);
        return;
    }

}
