package com.chatterjee.sayan.payzapp.vault.entities;

import com.chatterjee.sayan.payzapp.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(name = "vault_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaultCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private UUID id;

    @Column(nullable = false,length =4)
    private String lastFour;

    @Column(nullable = false,length = 6)
    private String bin;

    @Column(nullable = false)
    private byte[] encryptedPan;

    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String expiryMonth;

    @Column(nullable = false)
    private String expiryYear;

    @Column(nullable = false)
    private String cardHolderName;
    private LocalDateTime deletedAt;




}
