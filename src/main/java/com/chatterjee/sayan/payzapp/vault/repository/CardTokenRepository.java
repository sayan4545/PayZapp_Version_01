package com.chatterjee.sayan.payzapp.vault.repository;

import com.chatterjee.sayan.payzapp.vault.entities.CardToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CardTokenRepository extends JpaRepository<CardToken, UUID> {
    Optional<CardToken> findByTokenAndRevokedAtIsNull(String token);
}
