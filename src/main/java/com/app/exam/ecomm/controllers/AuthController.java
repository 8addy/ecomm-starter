package com.app.exam.ecomm.controllers;

import com.app.exam.ecomm.controllers.dto.AuthenticationRequest;
import com.app.exam.ecomm.controllers.dto.AuthenticationResponse;
import com.app.exam.ecomm.controllers.dto.RegisterRequest;
import com.app.exam.ecomm.controllers.dto.ResetPasswordRequest;
import com.app.exam.ecomm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return userService.resetPassword(resetPasswordRequest);
    }
}

