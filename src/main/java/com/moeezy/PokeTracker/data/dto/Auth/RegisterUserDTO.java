package com.moeezy.PokeTracker.data.dto.Auth;

public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;

    public RegisterUserDTO(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
