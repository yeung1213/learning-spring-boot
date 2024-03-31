package com.yeung.learning.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.yeung.learning.dtos.MyResponse;
import com.yeung.learning.services.OAuthService;

@RestController
@RequestMapping("api/oauth")
public class OAuthRestcontroller {
    @Autowired
    OAuthService oAuthService;

    @GetMapping("google/callback")
    public MyResponse oauth(@RequestParam("code") String code) {
        final MyResponse response = new MyResponse();
        if (code == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String result = oAuthService.getGoogleToken(code);
        response.setData(result);
        System.out.println(code);
        return response;
    }

}
