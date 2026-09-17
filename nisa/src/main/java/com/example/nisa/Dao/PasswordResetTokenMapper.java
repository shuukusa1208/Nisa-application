package com.example.nisa.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nisa.Entity.PasswordResetToken;

@Repository
public interface PasswordResetTokenMapper extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserEmail(String email);
}
