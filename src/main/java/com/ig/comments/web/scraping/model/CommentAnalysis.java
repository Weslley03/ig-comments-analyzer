package com.ig.comments.web.scraping.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "comment_analysis")
public class CommentAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ig_url", nullable = false, columnDefinition = "TEXT")
    private String igUrl;

    @Column(name = "description_content", columnDefinition = "TEXT")
    private String descriptionContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String result;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "JSONB")
    private Map<String, Object> metadata = new HashMap<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = OffsetDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIgUrl() { return igUrl; }
    public void setIgUrl(String igUrl) { this.igUrl = igUrl; }
    public String getDescriptionContent() { return descriptionContent; }
    public void setDescriptionContent(String descriptionContent) { this.descriptionContent = descriptionContent; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
