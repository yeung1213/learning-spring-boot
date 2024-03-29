package com.yeung.learning.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeung.learning.dtos.MyResponse;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/test")
public class TestRestcontroller {

    @GetMapping("")
    public MyResponse root() {
        final MyResponse response = new MyResponse();
        response.setData("Hello, World!");
        return response;
    }

}
