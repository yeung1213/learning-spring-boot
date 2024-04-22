package com.yeung.learning.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeung.learning.dtos.MyResponse;
import com.yeung.learning.services.TestingService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequestMapping("api/test-auth")
public class TestAuthRestcontroller {
    @GetMapping("")
    public MyResponse root(HttpSession session) {
        final MyResponse response = new MyResponse();
        System.out.println("Hello World I am in test-auth: " + session.getAttribute("testing"));
        session.setAttribute("testing", "testing");
        response.setData("Hello, World!");
        return response;
    }
}
