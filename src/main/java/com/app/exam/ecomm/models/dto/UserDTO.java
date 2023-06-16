package com.app.exam.ecomm.models.dto;

import com.app.exam.ecomm.models.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nom_utilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private Long tel;
    private Role role;

    public UserDTO(Long id, String nom_utilisateur, String nom, String prenom, String adesse_mail, String ville, Long tel, Role role) {
        this.id = id;
        this.nom_utilisateur = nom_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = adesse_mail;
        this.ville = ville;
        this.tel = tel;
        this.role = role;
    }
}
