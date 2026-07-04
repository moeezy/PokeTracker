package com.moeezy.PokeTracker.data.dto.Auth;

// Only DTO in this package that isn't a mirror of an entity/query result - it wraps the JWT
// handed back to the client after a successful login.
public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
