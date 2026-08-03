package com.chatterjee.sayan.payzapp.merchant.repositories;

import com.chatterjee.sayan.payzapp.merchant.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
}
