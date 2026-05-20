package com.example.apitestappbackend.models.hospitaldb;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "saved_searches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_saved_searches_user_test"))
    private UserTest userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_node_id", foreignKey = @ForeignKey(name = "fk_saved_searches_node_test"))
    private NodeTest targetNode;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Column(name = "searched_at")
    private Timestamp searchedAt;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String status = "success";

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "time_stamp", length = 50)
    private Timestamp timeStamp;

    @Column(name = "used_in_test")
    private Boolean usedInTest = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}
