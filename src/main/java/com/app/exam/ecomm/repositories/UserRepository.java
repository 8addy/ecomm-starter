package com.app.exam.ecomm.repositories;

import com.app.exam.ecomm.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.nom_utilisateur = :nomUtilisateur")
    Optional<User> findByNomUtilisateur(String nomUtilisateur);
}
