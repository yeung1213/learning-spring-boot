package com.yeung.learning.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

@Service
public class AzureKeyVaultTestService {
    @Value("${com.yeung.learning.azure-key-vault-testing-key}")
    private String keyVaultTestingKey;

    Logger logger = LoggerFactory.getLogger(AzureKeyVaultTestService.class);

    @Autowired
    private SecretClient secretClient;

    public String getKey() {
        logger.info("hello world 2" + keyVaultTestingKey);
        KeyVaultSecret watsonSecret = secretClient.getSecret(keyVaultTestingKey);
        logger.info(watsonSecret.getName());
        logger.info(watsonSecret.getValue());
        return "";
    }

}
