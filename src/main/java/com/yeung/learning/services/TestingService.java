package com.yeung.learning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class TestingService {
    @Autowired
    HttpSession httpSession;

    public void testing() {
        String result = (String) httpSession.getAttribute("testing");
        System.out.println("i am in testingService, val of testing is " + result);
    }
}
