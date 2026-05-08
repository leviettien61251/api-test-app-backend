package com.example.apitestappbackend.models.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "test_suite_items", indexes = {
        @Index(name = "idx_test_suite_id", columnList = "test_suite_id"),
        @Index(name = "idx_request_id", columnList = "request_id"),
        @Index(name = "idx_scenario_id", columnList = "scenario_id"),
        @Index(name = "idx_unique_item", columnList = "test_suite_id, request_id, scenario_id", unique = true)
})
public class TestSuiteItem {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "test_suite_id")
    private TestSuite testSuite;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private TestScenario scenario;

    @Column(name = "item_type", length = 20)
    private String itemType; // 'REQUEST', 'SCENARIO'

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "delay_before_ms")
    private Integer delayBeforeMs = 0;
}