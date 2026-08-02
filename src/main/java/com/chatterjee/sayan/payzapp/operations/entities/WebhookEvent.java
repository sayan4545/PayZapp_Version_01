package com.chatterjee.sayan.payzapp.operations.entities;

import com.chatterjee.sayan.payzapp.common.enums.WebhookEventStatus;
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
@Table(name = "webhook_event")
public class WebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID merchantId;

    private String eventType; // TODO : to be converted to enum while designing the Webhook

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> payload;

    @Column(nullable = false,length = 100)
    private String targetUrl;

    @Column(nullable = false,length = 100)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private WebhookEventStatus status;

    @Column(nullable = false)
    private Integer attempts;

    private LocalDateTime nextRetryAt;

    private LocalDateTime lastAttemptAt;

    @Column(nullable = false)
    private Integer lastResponseCode;

    @Column(nullable = false,length = 20)
    private String lastResponseBody;

    private LocalDateTime deliveredAt;
}
