package com.yeung.learning.restcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeung.learning.dtos.MyResponse;
import com.yeung.learning.services.AzureKeyVaultTestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/azure-key-vault-test")
public class AzureKeyVaultTestRestcontroller {
    @Autowired
    AzureKeyVaultTestService azureKeyVaultTestService;

    @GetMapping("{id}")
    public MyResponse getMethodName(@PathVariable("id") String id) {
        final MyResponse response = new MyResponse();
        azureKeyVaultTestService.getKey();
        response.setData(id);
        return response;
    }

}
