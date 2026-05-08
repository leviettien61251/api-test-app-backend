package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "client_sessions")
public class ClientSession {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "client_type", length = 50)
    private String clientType; // 'JAVAFX', 'WEB', 'CLI'

    @Column(name = "client_version", length = 20)
    private String clientVersion;

    @Column(name = "client_hostname", length = 255)
    private String clientHostname;

    @Column(name = "client_os", length = 100)
    private String clientOs;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "connected_at", updatable = false)
    private LocalDateTime connectedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
