package com.ig.comments.web.scraping.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_code")
public class RoleCode {

    @Id
    @Column(length = 100)
    private String role;

    @Column(name = "usage_limit", nullable = false)
    private Integer usageLimit;

    @Column(name = "comments_per_query", nullable = false)
    private Integer commentsPerQuery;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getCommentsPerQuery() { return commentsPerQuery; }
    public void setCommentsPerQuery(Integer commentsPerQuery) { this.commentsPerQuery = commentsPerQuery; }
}
