package com.moeezy.PokeTracker.data.dto.Auth;

// Only DTO in this package that isn't a mirror of an entity/query result - it wraps the JWT
// handed back to the client after a successful login.
public class AuthResponseDTO {
    private String token;
    private String username;
    private int id;

    public AuthResponseDTO(String token, String username, int id){
        this.token = token;
        this.username = username;
        this.id = id;
    }

    public String getToken(){
        return token;
    }

    public String getUsername(){
        return username;
    }

    public int getId(){
        return id;
    }
}
