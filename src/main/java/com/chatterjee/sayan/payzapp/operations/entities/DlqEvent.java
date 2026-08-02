package com.chatterjee.sayan.payzapp.operations.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Dlq_Event")
public class DlqEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false, length = 100)
    private String finalError;

    @OneToOne(fetch = FetchType.LAZY)
    private WebhookEvent webhookEvent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> payLoad;

    private LocalDateTime movedAt;

    private LocalDateTime replayedAt;
}
