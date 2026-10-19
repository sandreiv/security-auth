package com.app.ies.security.auth.domain.models;

public class Token {

    private String accessToken;
    private String refreshToken;
    private String type;
    //private String username;

    public Token() {
    }

    public Token(String accessToken, String refreshToken, String type) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = type;
        //this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /*public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }*/
}
