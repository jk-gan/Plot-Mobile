package com.example.jkgan.pmot.person;

/**
 * Created by JKGan on 03/11/2015.
 */
public class User {
    private String name;
    private String email;
    private String password;
    private String password_comfirmation;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword_comfirmation() {
        return password_comfirmation;
    }

    public void setPassword_comfirmation(String password_comfirmation) {
        this.password_comfirmation = password_comfirmation;
    }
}
