package com.suzano.inspection.controller;

import com.suzano.inspection.dto.LoginRequest;
import com.suzano.inspection.dto.LoginResponse;
import com.suzano.inspection.entity.User;
import com.suzano.inspection.security.JwtTokenUtil;
import com.suzano.inspection.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            
            final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            
            User user = userService.getUserByUsername(loginRequest.getUsername()).get();
            
            LoginResponse response = new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getDepartment(),
                user.getRole()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (DisabledException e) {
            return ResponseEntity.badRequest().body("User account is disabled");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok().body("{\"token\":\"" + refreshedToken + "\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token refresh failed: " + e.getMessage());
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

