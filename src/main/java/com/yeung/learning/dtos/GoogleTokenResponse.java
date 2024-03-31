package com.yeung.learning.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleTokenResponse {
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
        return "GoogleTokenResponse [idToken=" + idToken + "]";
    }

}
