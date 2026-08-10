package com.chatterjee.sayan.payzapp.vault.entities;

import com.chatterjee.sayan.payzapp.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "card_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 50,unique = true)
    private String token;

    // One vault card can generate multiple tokens
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false)
    private VaultCard vaultCard;

    @Column(nullable = false,length = 50)
    private UUID merchant;

    @Column(nullable = false,length = 50)
    private UUID customer;

    private LocalDateTime revokedAt;
}
