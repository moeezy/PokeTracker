package com.moeezy.PokeTracker.service;

import com.moeezy.PokeTracker.data.dto.Auth.AuthResponseDTO;
import com.moeezy.PokeTracker.data.dto.Auth.LoginUserDTO;
import com.moeezy.PokeTracker.data.dto.Auth.RegisterUserDTO;
import com.moeezy.PokeTracker.data.entity.User;
import com.moeezy.PokeTracker.data.repository.UserRepository;
import com.moeezy.PokeTracker.security.JwtTokenProvider;
import com.moeezy.PokeTracker.web.exception.InvalidCredentialsException;
import com.moeezy.PokeTracker.web.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Unlike the other services, this has to check for duplicates before writing - usernames/emails
    // must be unique or login can't identify a single account.
    @Transactional
    public void register(RegisterUserDTO registerUserDTO){
        if(userRepository.existsByUsername(registerUserDTO.getUsername())){
            throw new UserAlreadyExistsException("Username already taken: " + registerUserDTO.getUsername());
        }
        if(userRepository.existsByEmail(registerUserDTO.getEmail())){
            throw new UserAlreadyExistsException("Email already registered: " + registerUserDTO.getEmail());
        }

        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setEmail(registerUserDTO.getEmail());
        // Only the bcrypt hash is ever persisted - the raw password never touches the database.
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        userRepository.save(user);
    }

    // Credential checking is delegated to Spring Security's AuthenticationManager, which uses
    // UserDetailsConfig + the PasswordEncoder bean under the hood - this method just turns a
    // successful check into a JWT via JwtTokenProvider.
    public AuthResponseDTO login(LoginUserDTO loginUserDTO){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(loginUserDTO.getUsername());
        return new AuthResponseDTO(token);
    }
}
