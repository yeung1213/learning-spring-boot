package com.yeung.learning.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OktaTokenResponse
 */
public class OktaTokenResponse {
    @JsonProperty("id_token")
    private String idToken;

    public String geIdToken() {
        return idToken;
    }

    public void setToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "OktaTokenResponse [idToken=" + idToken + "]";
    }
    
}