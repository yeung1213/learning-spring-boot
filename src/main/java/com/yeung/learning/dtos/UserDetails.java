package com.yeung.learning.dtos;

public class UserDetails {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDetails [email=" + email + "]";
    }

}
