package com.app.exam.ecomm.services;

import com.app.exam.ecomm.config.JwtService;
import com.app.exam.ecomm.controllers.dto.AuthenticationRequest;
import com.app.exam.ecomm.controllers.dto.AuthenticationResponse;
import com.app.exam.ecomm.controllers.dto.RegisterRequest;
import com.app.exam.ecomm.models.entity.Role;
import com.app.exam.ecomm.models.entity.User;
import com.app.exam.ecomm.repositories.RoleRepository;
import com.app.exam.ecomm.repositories.UserRepository;
import com.app.exam.ecomm.response.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public AuthenticationResponse register(@NonNull RegisterRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        validateEmail(request.getEmail());
        validateUsername(request.getNom_utilisateur());
        validatePhoneNumber(request.getTel());
        var user = User.builder()
                .nom_utilisateur(request.getNom_utilisateur())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .ville(request.getVille())
                .tel(request.getTel())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public ResponseEntity<String> resetPassword(String newPassword) {
        try {
            User user = getAuthenticatedUser();

            // Set the new password for the user
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset password: " + e.getMessage());
        }
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    private void validateUsername(String nomUtilisateur) {
        Optional<User> userExist = userRepository.findByNomUtilisateur(nomUtilisateur);
        if (userExist.isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }
    }
    private void validateEmail(String email) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Email address is not valid");
        }

        Optional<User> userExist = userRepository.findByEmail(email);
        if (userExist.isPresent()) {
            throw new IllegalArgumentException("Email address is already in use");
        }
    }
    private boolean isEmailValid(String email) {
        // Regular expression pattern for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // Validate the email against the pattern
        return email.matches(emailRegex);
    }

    private void validatePhoneNumber(Long tel) {
        if (!isPhoneNumberValid(tel.toString())) {
            throw new IllegalArgumentException("Phone Number is not valid");
        }
    }
    public boolean isPhoneNumberValid(String phoneNumber) {
        // Regular expression pattern for phone number validation
        String pattern = "^(\\+[0-9]{1,3})?\\s?\\(?[0-9]{1,4}\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}$";
        // Compile the pattern into a regex object
        Pattern regex = Pattern.compile(pattern);
        // Create a matcher with the input phone number
        Matcher matcher = regex.matcher(phoneNumber);
        // Check if the phone number matches the pattern
        int minLength = 10;
        int maxLength = 15;
        return matcher.matches() && phoneNumber.length() >= minLength && phoneNumber.length() <= maxLength;
    }

}
