package com.app.exam.ecomm.controllers;

import com.app.exam.ecomm.models.dto.UserDTO;
import com.app.exam.ecomm.models.entity.Article;
import com.app.exam.ecomm.models.entity.User;
import com.app.exam.ecomm.response.ErrorMessage;
import com.app.exam.ecomm.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> listUsers = users.stream().map(user -> new UserDTO(user.getId(), user.getNom_utilisateur(), user.getNom(), user.getPrenom(), user.getEmail(), user.getVille(), user.getTel(), user.getRole())).collect(Collectors.toList());
        return ResponseEntity.ok(listUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userResponse = new UserDTO(user.getId(), user.getNom_utilisateur(), user.getNom(), user.getPrenom(), user.getEmail(), user.getVille(), user.getTel(), user.getRole());
            return ResponseEntity.ok(userResponse);
        } else {
            ErrorMessage errorMessage = new ErrorMessage("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


}
