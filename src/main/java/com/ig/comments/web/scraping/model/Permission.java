package com.ig.comments.web.scraping.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "role_code", nullable = false, length = 100)
    private String roleCode;

    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;

    @Column(name = "remaining_limit", nullable = false)
    private Integer remainingLimit;

    @Column(name = "comments_per_query", nullable = false)
    private Integer commentsPerQuery;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRoleCode() { return roleCode; }
    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getRemainingLimit() { return remainingLimit; }
    public void setRemainingLimit(Integer remainingLimit) { this.remainingLimit = remainingLimit; }
    public Integer getCommentsPerQuery() { return commentsPerQuery; }
    public void setCommentsPerQuery(Integer commentsPerQuery) { this.commentsPerQuery = commentsPerQuery; }
}
