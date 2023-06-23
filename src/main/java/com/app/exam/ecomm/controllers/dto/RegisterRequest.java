package com.app.exam.ecomm.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nom_utilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private Long tel;
    private String password;
    private Long roleId;
}
