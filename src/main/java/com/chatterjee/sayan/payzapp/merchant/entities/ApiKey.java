package com.chatterjee.sayan.payzapp.merchant.entities;

import com.chatterjee.sayan.payzapp.common.enums.Environment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "api_key", indexes = {
        @Index(name = "idx_apiKey_merchant_id",columnList = "merchant_id"),
        @Index(name = "idx_apiKey_merchant_env_enabled",columnList = "merchant_id,environment,enabled")
})
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    @Column(nullable = false,length = 50,unique = true)
    private String keyId;
    @Column(nullable = false,length = 200)
    private String keySecretHash;

    @Column(length = 200)
    private String previousKeySecretHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    private LocalDateTime lastUsedAt;
    private LocalDateTime rotatedAt;
    private LocalDateTime gracePeriodExpiresAt;

}
