package com.moeezy.PokeTracker.web;

import com.moeezy.PokeTracker.data.dto.Auth.AuthResponseDTO;
import com.moeezy.PokeTracker.data.dto.Auth.LoginUserDTO;
import com.moeezy.PokeTracker.data.dto.Auth.RegisterUserDTO;
import com.moeezy.PokeTracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Both endpoints here are listed in WebSecurityConfig's permitAll matchers - they have to be
// reachable without a JWT since they're how a client gets one in the first place.
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/v1/users")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserDTO registerUserDTO){
        authService.register(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginUserDTO loginUserDTO){
        AuthResponseDTO authResponse = authService.login(loginUserDTO);
        return ResponseEntity.ok(authResponse);
    }
}
